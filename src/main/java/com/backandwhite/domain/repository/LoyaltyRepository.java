package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyRepository {
    // Tiers
    LoyaltyTier saveTier(LoyaltyTier tier);

    LoyaltyTier updateTier(LoyaltyTier tier);

    Optional<LoyaltyTier> findTierById(String id);

    List<LoyaltyTier> findAllTiers();

    void deleteTier(String id);

    // Rules
    LoyaltyRule saveRule(LoyaltyRule rule);

    LoyaltyRule updateRule(LoyaltyRule rule);

    Optional<LoyaltyRule> findRuleById(String id);

    Optional<LoyaltyRule> findActiveRuleByAction(LoyaltyAction action);

    List<LoyaltyRule> findAllRules();

    void deleteRule(String id);

    // Transactions
    LoyaltyTransaction saveTransaction(LoyaltyTransaction transaction);

    Page<LoyaltyTransaction> findTransactionsByUserId(String userId, Pageable pageable);

    int sumPointsByUserId(String userId);

    /**
     * Check if an EARN transaction already exists for the given orderId
     * (idempotency).
     */
    boolean existsEarnTransactionByOrderId(String orderId);

    /**
     * Sum of all EARN points registered for an order (covers regular purchase
     * points + any level-up bonus awarded on the same order).
     */
    int sumEarnPointsByOrderId(String orderId);

    /**
     * Check if a compensating REDEEM transaction already exists for the given
     * orderId (idempotency for cancellation reversals).
     */
    boolean existsReverseTransactionByOrderId(String orderId);
}
