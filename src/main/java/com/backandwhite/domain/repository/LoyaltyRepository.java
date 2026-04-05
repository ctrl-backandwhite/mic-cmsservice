package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    List<LoyaltyRule> findAllRules();

    void deleteRule(String id);

    // Transactions
    LoyaltyTransaction saveTransaction(LoyaltyTransaction transaction);

    Page<LoyaltyTransaction> findTransactionsByUserId(String userId, Pageable pageable);

    int sumPointsByUserId(String userId);
}
