package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTierEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTransactionEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.LoyaltyInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyRuleJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyTierJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.LoyaltyTransactionJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class LoyaltyRepositoryImplTest {

    @Mock
    private LoyaltyTierJpaRepository tierJpa;

    @Mock
    private LoyaltyRuleJpaRepository ruleJpa;

    @Mock
    private LoyaltyTransactionJpaRepository transactionJpa;

    @Mock
    private LoyaltyInfraMapper mapper;

    private LoyaltyRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new LoyaltyRepositoryImpl(tierJpa, ruleJpa, transactionJpa, mapper);
    }

    private LoyaltyTier tier() {
        return LoyaltyTier.builder().name("bronze").build();
    }

    private LoyaltyRule rule() {
        return LoyaltyRule.builder().action(LoyaltyAction.PURCHASE).pointsPerUnit(1).active(true).build();
    }

    private LoyaltyTransaction tx() {
        return LoyaltyTransaction.builder().userId("u1").points(10).type(LoyaltyTransactionType.EARN).build();
    }

    // Tiers

    @Test
    void saveTier_assignsId() {
        LoyaltyTier in = tier();
        LoyaltyTierEntity entity = new LoyaltyTierEntity();
        LoyaltyTierEntity saved = new LoyaltyTierEntity();
        LoyaltyTier out = tier();

        when(mapper.toTierEntity(in)).thenReturn(entity);
        when(tierJpa.save(entity)).thenReturn(saved);
        when(mapper.toTierDomain(saved)).thenReturn(out);

        LoyaltyTier r = repo.saveTier(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void updateTier_keepsId() {
        LoyaltyTier in = tier();
        in.setId("kept");
        LoyaltyTierEntity entity = new LoyaltyTierEntity();
        LoyaltyTierEntity saved = new LoyaltyTierEntity();
        LoyaltyTier out = tier();

        when(mapper.toTierEntity(in)).thenReturn(entity);
        when(tierJpa.save(entity)).thenReturn(saved);
        when(mapper.toTierDomain(saved)).thenReturn(out);

        assertThat(repo.updateTier(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("kept");
    }

    @Test
    void findTierById_presentAndEmpty() {
        LoyaltyTierEntity e = new LoyaltyTierEntity();
        when(tierJpa.findById("t1")).thenReturn(Optional.of(e));
        when(mapper.toTierDomain(e)).thenReturn(tier());
        assertThat(repo.findTierById("t1")).isPresent();

        when(tierJpa.findById("t2")).thenReturn(Optional.empty());
        assertThat(repo.findTierById("t2")).isEmpty();
    }

    @Test
    void findAllTiers_delegates() {
        LoyaltyTierEntity e = new LoyaltyTierEntity();
        when(tierJpa.findAllByOrderByMinPointsAsc()).thenReturn(List.of(e));
        when(mapper.toTierDomain(e)).thenReturn(tier());
        assertThat(repo.findAllTiers()).hasSize(1);
    }

    @Test
    void deleteTier_delegates() {
        repo.deleteTier("t1");
        verify(tierJpa).deleteById("t1");
    }

    // Rules

    @Test
    void saveRule_assignsId() {
        LoyaltyRule in = rule();
        LoyaltyRuleEntity entity = new LoyaltyRuleEntity();
        LoyaltyRuleEntity saved = new LoyaltyRuleEntity();
        LoyaltyRule out = rule();

        when(mapper.toRuleEntity(in)).thenReturn(entity);
        when(ruleJpa.save(entity)).thenReturn(saved);
        when(mapper.toRuleDomain(saved)).thenReturn(out);

        LoyaltyRule r = repo.saveRule(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void updateRule_keepsId() {
        LoyaltyRule in = rule();
        in.setId("kept");
        LoyaltyRuleEntity entity = new LoyaltyRuleEntity();
        LoyaltyRuleEntity saved = new LoyaltyRuleEntity();
        LoyaltyRule out = rule();

        when(mapper.toRuleEntity(in)).thenReturn(entity);
        when(ruleJpa.save(entity)).thenReturn(saved);
        when(mapper.toRuleDomain(saved)).thenReturn(out);

        assertThat(repo.updateRule(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("kept");
    }

    @Test
    void findRuleById_presentAndEmpty() {
        LoyaltyRuleEntity e = new LoyaltyRuleEntity();
        when(ruleJpa.findById("r1")).thenReturn(Optional.of(e));
        when(mapper.toRuleDomain(e)).thenReturn(rule());
        assertThat(repo.findRuleById("r1")).isPresent();

        when(ruleJpa.findById("r2")).thenReturn(Optional.empty());
        assertThat(repo.findRuleById("r2")).isEmpty();
    }

    @Test
    void findActiveRuleByAction_present() {
        LoyaltyRuleEntity e = new LoyaltyRuleEntity();
        when(ruleJpa.findFirstByActionAndActiveTrueOrderByCreatedAtDesc(LoyaltyAction.PURCHASE))
                .thenReturn(Optional.of(e));
        when(mapper.toRuleDomain(e)).thenReturn(rule());
        assertThat(repo.findActiveRuleByAction(LoyaltyAction.PURCHASE)).isPresent();
    }

    @Test
    void findActiveRuleByAction_empty() {
        when(ruleJpa.findFirstByActionAndActiveTrueOrderByCreatedAtDesc(LoyaltyAction.REVIEW))
                .thenReturn(Optional.empty());
        assertThat(repo.findActiveRuleByAction(LoyaltyAction.REVIEW)).isEmpty();
    }

    @Test
    void findAllRules_delegates() {
        LoyaltyRuleEntity e = new LoyaltyRuleEntity();
        when(ruleJpa.findAll()).thenReturn(List.of(e));
        when(mapper.toRuleDomain(e)).thenReturn(rule());
        assertThat(repo.findAllRules()).hasSize(1);
    }

    @Test
    void deleteRule_delegates() {
        repo.deleteRule("r1");
        verify(ruleJpa).deleteById("r1");
    }

    // Transactions

    @Test
    void saveTransaction_assignsId() {
        LoyaltyTransaction in = tx();
        LoyaltyTransactionEntity entity = new LoyaltyTransactionEntity();
        LoyaltyTransactionEntity saved = new LoyaltyTransactionEntity();
        LoyaltyTransaction out = tx();

        when(mapper.toTransactionEntity(in)).thenReturn(entity);
        when(transactionJpa.save(entity)).thenReturn(saved);
        when(mapper.toTransactionDomain(saved)).thenReturn(out);

        LoyaltyTransaction r = repo.saveTransaction(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void findTransactionsByUserId_paged() {
        LoyaltyTransactionEntity e = new LoyaltyTransactionEntity();
        Page<LoyaltyTransactionEntity> page = new PageImpl<>(List.of(e));
        when(transactionJpa.findByUserIdOrderByCreatedAtDesc(eq("u1"), any(Pageable.class))).thenReturn(page);
        when(mapper.toTransactionDomain(e)).thenReturn(tx());

        assertThat(repo.findTransactionsByUserId("u1", PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void sumPointsByUserId_nonNull() {
        when(transactionJpa.sumPointsByUserId("u1")).thenReturn(42);
        assertThat(repo.sumPointsByUserId("u1")).isEqualTo(42);
    }

    @Test
    void sumPointsByUserId_zeroFromJpa_returnsZero() {
        when(transactionJpa.sumPointsByUserId("u1")).thenReturn(0);
        assertThat(repo.sumPointsByUserId("u1")).isZero();
    }

    @Test
    void existsEarnTransactionByOrderId_true() {
        when(transactionJpa.existsByOrderIdAndType("o1", LoyaltyTransactionType.EARN)).thenReturn(true);
        assertThat(repo.existsEarnTransactionByOrderId("o1")).isTrue();
    }

    @Test
    void existsEarnTransactionByOrderId_false() {
        when(transactionJpa.existsByOrderIdAndType("o1", LoyaltyTransactionType.EARN)).thenReturn(false);
        assertThat(repo.existsEarnTransactionByOrderId("o1")).isFalse();
    }

    @Test
    void sumEarnPointsByOrderId_delegates() {
        when(transactionJpa.sumPointsByOrderIdAndType("o1", LoyaltyTransactionType.EARN)).thenReturn(15);
        assertThat(repo.sumEarnPointsByOrderId("o1")).isEqualTo(15);
    }

    @Test
    void existsReverseTransactionByOrderId_true() {
        when(transactionJpa.existsByOrderIdAndType("o1", LoyaltyTransactionType.REDEEM)).thenReturn(true);
        assertThat(repo.existsReverseTransactionByOrderId("o1")).isTrue();
    }
}
