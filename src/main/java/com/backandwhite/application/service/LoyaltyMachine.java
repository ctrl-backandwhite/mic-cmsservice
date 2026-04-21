package com.backandwhite.application.service;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.repository.LoyaltyRepository;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LoyaltyMachine — Motor centralizado del programa de fidelización.
 *
 * Responsabilidades: 1. Calcular puntos por compra: floor(totalUSD ×
 * pointsPerUnit × tierMultiplier) 2. Resolver el tier del usuario según su
 * balance de puntos 3. Detectar cambios de nivel (level-up) 4. Otorgar puntos
 * bonus al subir de nivel 5. Garantizar idempotencia (no duplicar puntos por la
 * misma orden) 6. Publicar eventos de loyalty a Kafka
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class LoyaltyMachine {

    private final LoyaltyRepository loyaltyRepository;
    private final CmsEventPort cmsEventPort;

    /**
     * Bonus fijo otorgado al subir de nivel (configurable en futuras versiones).
     */
    private static final int LEVEL_UP_BONUS = 100;

    /**
     * Procesa la entrega de una orden y otorga puntos de loyalty.
     *
     * Fórmula: earnedPoints = floor(totalAmountUsd × pointsPerUnit ×
     * tierMultiplier)
     *
     * Si el usuario sube de tier, recibe un bonus adicional de
     * {@value LEVEL_UP_BONUS} puntos.
     *
     * @param userId
     *            ID del usuario
     * @param totalAmountUsd
     *            Monto total de la orden en USD
     * @param orderId
     *            ID de la orden (para idempotencia y trazabilidad)
     * @return Resultado con puntos ganados, bonus, balance y tier info
     */
    @Transactional
    public LoyaltyResult processOrderDelivery(String userId, BigDecimal totalAmountUsd, String orderId) {
        // ── 0. Idempotencia: verificar que no se hayan otorgado puntos para esta orden
        // ──
        if (orderId != null && loyaltyRepository.existsEarnTransactionByOrderId(orderId)) {
            log.warn("::> Loyalty points already awarded for order={}, skipping", orderId);
            int balance = loyaltyRepository.sumPointsByUserId(userId);
            LoyaltyTier currentTier = resolveTier(balance);
            return LoyaltyResult.builder().earnedPoints(0).bonusPoints(0).totalBalance(balance)
                    .currentTier(currentTier != null ? currentTier.getName() : "Bronze").leveledUp(false).build();
        }

        // ── 1. Obtener regla PURCHASE activa → pointsPerUnit (default: 1) ──
        int pointsPerUnit = loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE)
                .map(LoyaltyRule::getPointsPerUnit).orElse(1);

        // ── 2. Balance y tier ANTES de esta compra ──
        int balanceBefore = loyaltyRepository.sumPointsByUserId(userId);
        LoyaltyTier tierBefore = resolveTier(balanceBefore);
        BigDecimal multiplier = (tierBefore != null && tierBefore.getMultiplier() != null)
                ? tierBefore.getMultiplier()
                : BigDecimal.ONE;

        // ── 3. Calcular puntos de la compra ──
        // Formula: floor(totalUSD × pointsPerUnit × multiplier)
        int earnedPoints = totalAmountUsd.multiply(BigDecimal.valueOf(pointsPerUnit)).multiply(multiplier).intValue(); // truncates
                                                                                                                       // toward
                                                                                                                       // zero

        if (earnedPoints <= 0) {
            log.debug("::> No loyalty points for order={} (amount={}, ppUnit={}, mult={})", orderId, totalAmountUsd,
                    pointsPerUnit, multiplier);
            return LoyaltyResult.empty();
        }

        // ── 4. Registrar transacción EARN (compra) ──
        loyaltyRepository.saveTransaction(
                LoyaltyTransaction.builder().userId(userId).points(earnedPoints).type(LoyaltyTransactionType.EARN)
                        .description("Purchase reward: " + earnedPoints + " pts" + " (x"
                                + multiplier.stripTrailingZeros().toPlainString() + " "
                                + (tierBefore != null ? tierBefore.getName() : "Base") + ")")
                        .orderId(orderId).createdAt(Instant.now()).build());

        // ── 5. Calcular nuevo balance y resolver nuevo tier ──
        int balanceAfter = balanceBefore + earnedPoints;
        LoyaltyTier tierAfter = resolveTier(balanceAfter);

        // ── 6. Detectar level-up ──
        boolean leveledUp = tierAfter != null && (tierBefore == null || !tierAfter.getId().equals(tierBefore.getId()));
        int bonusPoints = 0;

        if (leveledUp) {
            bonusPoints = LEVEL_UP_BONUS;

            loyaltyRepository.saveTransaction(LoyaltyTransaction.builder().userId(userId).points(bonusPoints)
                    .type(LoyaltyTransactionType.EARN)
                    .description("🎉 Level up bonus! Welcome to " + tierAfter.getName() + " (+" + bonusPoints + " pts)")
                    .orderId(orderId).createdAt(Instant.now()).build());

            balanceAfter += bonusPoints;

            log.info("::> 🎉 User {} leveled up: {} → {} (+{} bonus pts)", userId,
                    tierBefore != null ? tierBefore.getName() : "None", tierAfter.getName(), bonusPoints);
        }

        // ── 7. Publicar evento de puntos ganados ──
        String tierName = tierAfter != null ? tierAfter.getName() : "Bronze";
        cmsEventPort.publishLoyaltyPointsEarned(userId, orderId, earnedPoints + bonusPoints, balanceAfter, tierName);

        log.info("::> Loyalty processed: user={}, earned={}, bonus={}, total={}, tier={}, levelUp={}", userId,
                earnedPoints, bonusPoints, balanceAfter, tierName, leveledUp);

        return LoyaltyResult.builder().earnedPoints(earnedPoints).bonusPoints(bonusPoints).totalBalance(balanceAfter)
                .previousTier(tierBefore != null ? tierBefore.getName() : null).currentTier(tierName)
                .leveledUp(leveledUp).build();
    }

    /**
     * Reverses the loyalty points awarded for a cancelled order. Looks up the total
     * EARN points registered for the {@code orderId} (regular reward + any level-up
     * bonus) and writes a compensating REDEEM transaction with the negated amount.
     *
     * <p>
     * Idempotent: if a REDEEM already exists for the orderId, or no EARN was ever
     * registered, the method is a no-op.
     */
    @Transactional
    public void reverseOrderPoints(String userId, String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return;
        }
        if (loyaltyRepository.existsReverseTransactionByOrderId(orderId)) {
            log.info("::> Loyalty reversal already applied for order={}, skipping", orderId);
            return;
        }
        int awarded = loyaltyRepository.sumEarnPointsByOrderId(orderId);
        if (awarded <= 0) {
            log.debug("::> No loyalty points to reverse for order={} (nothing awarded)", orderId);
            return;
        }
        loyaltyRepository.saveTransaction(LoyaltyTransaction.builder().userId(userId).points(-awarded)
                .type(LoyaltyTransactionType.REDEEM).description("Reversal: order cancelled (-" + awarded + " pts)")
                .orderId(orderId).createdAt(Instant.now()).build());
        log.info("::> Reversed {} loyalty points for cancelled order={}, user={}", awarded, orderId, userId);
    }

    /**
     * Resuelve el tier de un usuario dado su balance de puntos. Itera los tiers
     * ordenados por minPoints ASC; el último que matchea gana.
     *
     * @param points
     *            Balance de puntos acumulados
     * @return El tier correspondiente, o null si no hay tiers configurados
     */
    public LoyaltyTier resolveTier(int points) {
        List<LoyaltyTier> tiers = loyaltyRepository.findAllTiers(); // sorted by minPoints ASC
        LoyaltyTier resolved = null;
        for (LoyaltyTier tier : tiers) {
            if (points >= tier.getMinPoints()) {
                resolved = tier;
            } else {
                break;
            }
        }
        return resolved;
    }
}
