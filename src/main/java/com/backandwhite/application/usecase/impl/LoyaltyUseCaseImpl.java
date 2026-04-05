package com.backandwhite.application.usecase.impl;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.LoyaltyUseCase;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.repository.LoyaltyRepository;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import com.backandwhite.infrastructure.message.kafka.producer.CmsEventProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;
import static com.backandwhite.domain.exception.Message.LOYALTY_INSUFFICIENT_POINTS;

@Service
@RequiredArgsConstructor
public class LoyaltyUseCaseImpl implements LoyaltyUseCase {

    private final LoyaltyRepository loyaltyRepository;
    private final Optional<CmsEventProducerService> cmsEventProducer;

    // Tiers

    @Override
    @Transactional
    public LoyaltyTier createTier(LoyaltyTier tier) {
        return loyaltyRepository.saveTier(tier);
    }

    @Override
    @Transactional
    public LoyaltyTier updateTier(String id, LoyaltyTier tier) {
        loyaltyRepository.findTierById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyTier", id));
        tier.setId(id);
        return loyaltyRepository.updateTier(tier);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyTier findTierById(String id) {
        return loyaltyRepository.findTierById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyTier", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoyaltyTier> findAllTiers() {
        return loyaltyRepository.findAllTiers();
    }

    @Override
    @Transactional
    public void deleteTier(String id) {
        loyaltyRepository.findTierById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyTier", id));
        loyaltyRepository.deleteTier(id);
    }

    // Rules

    @Override
    @Transactional
    public LoyaltyRule createRule(LoyaltyRule rule) {
        return loyaltyRepository.saveRule(rule);
    }

    @Override
    @Transactional
    public LoyaltyRule updateRule(String id, LoyaltyRule rule) {
        loyaltyRepository.findRuleById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyRule", id));
        rule.setId(id);
        return loyaltyRepository.updateRule(rule);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyRule findRuleById(String id) {
        return loyaltyRepository.findRuleById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyRule", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoyaltyRule> findAllRules() {
        return loyaltyRepository.findAllRules();
    }

    @Override
    @Transactional
    public void deleteRule(String id) {
        loyaltyRepository.findRuleById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("LoyaltyRule", id));
        loyaltyRepository.deleteRule(id);
    }

    // User points

    @Override
    @Transactional(readOnly = true)
    public int getBalance(String userId) {
        return loyaltyRepository.sumPointsByUserId(userId);
    }

    @Override
    @Transactional
    public LoyaltyTransaction earnPoints(String userId, int points, String description, String orderId) {
        LoyaltyTransaction tx = loyaltyRepository.saveTransaction(LoyaltyTransaction.builder()
                .userId(userId)
                .points(points)
                .type(LoyaltyTransactionType.EARN)
                .description(description)
                .orderId(orderId)
                .createdAt(Instant.now())
                .build());
        int totalPoints = loyaltyRepository.sumPointsByUserId(userId);
        cmsEventProducer.ifPresent(p -> p.publishLoyaltyPointsEarned(
                userId, orderId, points, totalPoints, null));
        return tx;
    }

    @Override
    @Transactional
    public LoyaltyTransaction redeemPoints(String userId, int points, String description, String orderId) {
        int balance = loyaltyRepository.sumPointsByUserId(userId);
        if (balance < points) {
            throw LOYALTY_INSUFFICIENT_POINTS.toBusinessException(balance);
        }
        LoyaltyTransaction tx = loyaltyRepository.saveTransaction(LoyaltyTransaction.builder()
                .userId(userId)
                .points(-points)
                .type(LoyaltyTransactionType.REDEEM)
                .description(description)
                .orderId(orderId)
                .createdAt(Instant.now())
                .build());
        int totalPoints = loyaltyRepository.sumPointsByUserId(userId);
        cmsEventProducer.ifPresent(p -> p.publishLoyaltyPointsRedeemed(
                userId, points, totalPoints, null, null));
        return tx;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationDtoOut<LoyaltyTransaction> getHistory(String userId, int page, int size, String sortBy,
            boolean ascending) {
        var pageable = PageableUtils.toPageable(page, size, sortBy, ascending);
        return PageableUtils.toResponse(loyaltyRepository.findTransactionsByUserId(userId, pageable));
    }
}
