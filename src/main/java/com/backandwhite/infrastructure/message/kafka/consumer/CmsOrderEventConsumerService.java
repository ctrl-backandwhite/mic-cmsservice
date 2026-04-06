package com.backandwhite.infrastructure.message.kafka.consumer;

import com.backandwhite.application.usecase.LoyaltyUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.core.kafka.avro.OrderConfirmedEvent;
import com.backandwhite.core.kafka.avro.OrderDeliveredEvent;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.repository.LoyaltyRepository;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Consumes order events to award loyalty points.
 * Points are awarded on order delivery using formula:
 * points = floor(orderTotal × pointsPerUnit × tierMultiplier)
 */
@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class CmsOrderEventConsumerService {

    private final LoyaltyUseCase loyaltyUseCase;
    private final LoyaltyRepository loyaltyRepository;

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_DELIVERED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderDelivered(OrderDeliveredEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        String totalAmount = str(event.getTotalAmount());
        log.info("::> Received order.delivered in CMS: orderId={}, userId={}, total={}",
                orderId, userId, totalAmount);
        try {
            BigDecimal amount = parseAmount(totalAmount);
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                return;

            // Resolve pointsPerUnit from PURCHASE rule (default: 1)
            int pointsPerUnit = loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE)
                    .map(LoyaltyRule::getPointsPerUnit)
                    .orElse(1);

            // Resolve tier multiplier based on current user balance
            BigDecimal multiplier = resolveTierMultiplier(userId);

            // Formula: floor(amount × pointsPerUnit × multiplier)
            int points = amount
                    .multiply(BigDecimal.valueOf(pointsPerUnit))
                    .multiply(multiplier)
                    .intValue(); // truncates toward zero

            if (points > 0) {
                loyaltyUseCase.earnPoints(userId, points, "Order delivery reward", orderId);
                log.info("::> Awarded {} loyalty points to user={} for order={} (ppUnit={}, tierMult={})",
                        points, userId, orderId, pointsPerUnit, multiplier);
            }
        } catch (Exception e) {
            log.error("::> Failed to award loyalty points for order={}: {}",
                    orderId, e.getMessage(), e);
        }
    }

    /**
     * Finds the user's current tier based on accumulated points and returns its
     * multiplier.
     * Falls back to 1.0 if no tier matches.
     */
    private BigDecimal resolveTierMultiplier(String userId) {
        int currentPoints = loyaltyRepository.sumPointsByUserId(userId);
        List<LoyaltyTier> tiers = loyaltyRepository.findAllTiers(); // sorted by minPoints ASC
        BigDecimal multiplier = BigDecimal.ONE;
        for (LoyaltyTier tier : tiers) {
            if (currentPoints >= tier.getMinPoints()) {
                multiplier = tier.getMultiplier() != null ? tier.getMultiplier() : BigDecimal.ONE;
            } else {
                break;
            }
        }
        return multiplier;
    }

    private String str(CharSequence cs) {
        return cs != null ? cs.toString() : null;
    }

    private BigDecimal parseAmount(String amount) {
        if (amount == null || amount.isBlank())
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
