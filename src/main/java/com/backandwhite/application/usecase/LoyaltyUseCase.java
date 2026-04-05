package com.backandwhite.application.usecase;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import java.util.List;

public interface LoyaltyUseCase {
    // Tiers
    LoyaltyTier createTier(LoyaltyTier tier);

    LoyaltyTier updateTier(String id, LoyaltyTier tier);

    LoyaltyTier findTierById(String id);

    List<LoyaltyTier> findAllTiers();

    void deleteTier(String id);

    // Rules
    LoyaltyRule createRule(LoyaltyRule rule);

    LoyaltyRule updateRule(String id, LoyaltyRule rule);

    LoyaltyRule findRuleById(String id);

    List<LoyaltyRule> findAllRules();

    void deleteRule(String id);

    // User points
    int getBalance(String userId);

    LoyaltyTransaction earnPoints(String userId, int points, String description, String orderId);

    LoyaltyTransaction redeemPoints(String userId, int points, String description, String orderId);

    PaginationDtoOut<LoyaltyTransaction> getHistory(String userId, int page, int size, String sortBy,
            boolean ascending);
}
