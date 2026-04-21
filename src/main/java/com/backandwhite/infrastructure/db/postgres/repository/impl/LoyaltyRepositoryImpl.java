package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.repository.LoyaltyRepository;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import com.backandwhite.infrastructure.db.postgres.mapper.LoyaltyInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyRuleJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyTierJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyTransactionJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyRepositoryImpl implements LoyaltyRepository {

    private final LoyaltyTierJpaRepository tierJpa;
    private final LoyaltyRuleJpaRepository ruleJpa;
    private final LoyaltyTransactionJpaRepository transactionJpa;
    private final LoyaltyInfraMapper mapper;

    // Tiers

    @Override
    public LoyaltyTier saveTier(LoyaltyTier tier) {
        tier.setId(UUID.randomUUID().toString());
        return mapper.toTierDomain(tierJpa.save(mapper.toTierEntity(tier)));
    }

    @Override
    public LoyaltyTier updateTier(LoyaltyTier tier) {
        return mapper.toTierDomain(tierJpa.save(mapper.toTierEntity(tier)));
    }

    @Override
    public Optional<LoyaltyTier> findTierById(String id) {
        return tierJpa.findById(id).map(mapper::toTierDomain);
    }

    @Override
    public List<LoyaltyTier> findAllTiers() {
        return tierJpa.findAllByOrderByMinPointsAsc().stream().map(mapper::toTierDomain).toList();
    }

    @Override
    public void deleteTier(String id) {
        tierJpa.deleteById(id);
    }

    // Rules

    @Override
    public LoyaltyRule saveRule(LoyaltyRule rule) {
        rule.setId(UUID.randomUUID().toString());
        return mapper.toRuleDomain(ruleJpa.save(mapper.toRuleEntity(rule)));
    }

    @Override
    public LoyaltyRule updateRule(LoyaltyRule rule) {
        return mapper.toRuleDomain(ruleJpa.save(mapper.toRuleEntity(rule)));
    }

    @Override
    public Optional<LoyaltyRule> findRuleById(String id) {
        return ruleJpa.findById(id).map(mapper::toRuleDomain);
    }

    @Override
    public Optional<LoyaltyRule> findActiveRuleByAction(com.backandwhite.domain.valueobject.LoyaltyAction action) {
        return ruleJpa.findByActionAndActiveTrue(action).map(mapper::toRuleDomain);
    }

    @Override
    public List<LoyaltyRule> findAllRules() {
        return ruleJpa.findAll().stream().map(mapper::toRuleDomain).toList();
    }

    @Override
    public void deleteRule(String id) {
        ruleJpa.deleteById(id);
    }

    // Transactions

    @Override
    public LoyaltyTransaction saveTransaction(LoyaltyTransaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        return mapper.toTransactionDomain(transactionJpa.save(mapper.toTransactionEntity(transaction)));
    }

    @Override
    public Page<LoyaltyTransaction> findTransactionsByUserId(String userId, Pageable pageable) {
        return transactionJpa.findByUserIdOrderByCreatedAtDesc(userId, pageable).map(mapper::toTransactionDomain);
    }

    @Override
    public int sumPointsByUserId(String userId) {
        Integer sum = transactionJpa.sumPointsByUserId(userId);
        return sum != null ? sum : 0;
    }

    @Override
    public boolean existsEarnTransactionByOrderId(String orderId) {
        return transactionJpa.existsByOrderIdAndType(orderId, LoyaltyTransactionType.EARN);
    }

    @Override
    public int sumEarnPointsByOrderId(String orderId) {
        return transactionJpa.sumPointsByOrderIdAndType(orderId, LoyaltyTransactionType.EARN);
    }

    @Override
    public boolean existsReverseTransactionByOrderId(String orderId) {
        return transactionJpa.existsByOrderIdAndType(orderId, LoyaltyTransactionType.REDEEM);
    }
}
