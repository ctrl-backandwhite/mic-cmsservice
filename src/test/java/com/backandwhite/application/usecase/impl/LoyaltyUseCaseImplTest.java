package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.common.exception.BusinessException;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.repository.LoyaltyRepository;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class LoyaltyUseCaseImplTest {

    @Mock
    private LoyaltyRepository repository;

    @Mock
    private CmsEventPort eventPort;

    @InjectMocks
    private LoyaltyUseCaseImpl useCase;

    private LoyaltyTier tier() {
        return LoyaltyTier.builder().id("t1").name("Bronze").minPoints(0).maxPoints(100).multiplier(BigDecimal.ONE)
                .build();
    }

    private LoyaltyRule rule() {
        return LoyaltyRule.builder().id("r1").action(LoyaltyAction.PURCHASE).pointsPerUnit(1).active(true).build();
    }

    // Tiers
    @Test
    void createTier_delegates() {
        LoyaltyTier t = tier();
        when(repository.saveTier(t)).thenReturn(t);
        assertThat(useCase.createTier(t)).isSameAs(t);
    }

    @Test
    void updateTier_existing() {
        when(repository.findTierById("t1")).thenReturn(Optional.of(tier()));
        when(repository.updateTier(any())).thenAnswer(i -> i.getArgument(0));
        LoyaltyTier upd = tier().withId(null).withName("Gold");
        LoyaltyTier result = useCase.updateTier("t1", upd);
        assertThat(result.getId()).isEqualTo("t1");
    }

    @Test
    void updateTier_notFound() {
        when(repository.findTierById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.updateTier("x", tier())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findTierById_existing() {
        when(repository.findTierById("t1")).thenReturn(Optional.of(tier()));
        assertThat(useCase.findTierById("t1")).isNotNull();
    }

    @Test
    void findTierById_notFound() {
        when(repository.findTierById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findTierById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAllTiers_delegates() {
        List<LoyaltyTier> list = List.of(tier());
        when(repository.findAllTiers()).thenReturn(list);
        assertThat(useCase.findAllTiers()).isSameAs(list);
    }

    @Test
    void deleteTier_existing() {
        when(repository.findTierById("t1")).thenReturn(Optional.of(tier()));
        useCase.deleteTier("t1");
        verify(repository).deleteTier("t1");
    }

    @Test
    void deleteTier_notFound() {
        when(repository.findTierById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.deleteTier("x")).isInstanceOf(EntityNotFoundException.class);
    }

    // Rules
    @Test
    void createRule_delegates() {
        LoyaltyRule r = rule();
        when(repository.saveRule(r)).thenReturn(r);
        assertThat(useCase.createRule(r)).isSameAs(r);
    }

    @Test
    void updateRule_existing() {
        when(repository.findRuleById("r1")).thenReturn(Optional.of(rule()));
        when(repository.updateRule(any())).thenAnswer(i -> i.getArgument(0));
        LoyaltyRule result = useCase.updateRule("r1", rule().withId(null));
        assertThat(result.getId()).isEqualTo("r1");
    }

    @Test
    void updateRule_notFound() {
        when(repository.findRuleById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.updateRule("x", rule())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findRuleById_existing() {
        when(repository.findRuleById("r1")).thenReturn(Optional.of(rule()));
        assertThat(useCase.findRuleById("r1")).isNotNull();
    }

    @Test
    void findRuleById_notFound() {
        when(repository.findRuleById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findRuleById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAllRules_delegates() {
        List<LoyaltyRule> list = List.of(rule());
        when(repository.findAllRules()).thenReturn(list);
        assertThat(useCase.findAllRules()).isSameAs(list);
    }

    @Test
    void deleteRule_existing() {
        when(repository.findRuleById("r1")).thenReturn(Optional.of(rule()));
        useCase.deleteRule("r1");
        verify(repository).deleteRule("r1");
    }

    @Test
    void deleteRule_notFound() {
        when(repository.findRuleById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.deleteRule("x")).isInstanceOf(EntityNotFoundException.class);
    }

    // User points
    @Test
    void getBalance_delegates() {
        when(repository.sumPointsByUserId("u1")).thenReturn(42);
        assertThat(useCase.getBalance("u1")).isEqualTo(42);
    }

    @Test
    void earnPoints_savesAndPublishes() {
        when(repository.saveTransaction(any(LoyaltyTransaction.class))).thenAnswer(i -> i.getArgument(0));
        when(repository.sumPointsByUserId("u1")).thenReturn(100);
        LoyaltyTransaction tx = useCase.earnPoints("u1", 10, "desc", "o1");
        assertThat(tx.getPoints()).isEqualTo(10);
        verify(eventPort).publishLoyaltyPointsEarned("u1", "o1", 10, 100, null);
    }

    @Test
    void redeemPoints_sufficientBalance_savesAndPublishes() {
        when(repository.sumPointsByUserId("u1")).thenReturn(100, 90);
        when(repository.saveTransaction(any(LoyaltyTransaction.class))).thenAnswer(i -> i.getArgument(0));

        LoyaltyTransaction tx = useCase.redeemPoints("u1", 10, "d", "o1");
        assertThat(tx.getPoints()).isEqualTo(-10);
        verify(eventPort).publishLoyaltyPointsRedeemed("u1", 10, 90, null, null);
    }

    @Test
    void redeemPoints_insufficient_throws() {
        when(repository.sumPointsByUserId("u1")).thenReturn(5);
        assertThatThrownBy(() -> useCase.redeemPoints("u1", 10, "d", "o1")).isInstanceOf(BusinessException.class);
    }

    @Test
    void getHistory_asc() {
        Page<LoyaltyTransaction> p = new PageImpl<>(List.of(LoyaltyTransaction.builder().id("tx1").build()));
        when(repository.findTransactionsByUserId(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.getHistory("u1", 0, 10, "createdAt", true).content()).hasSize(1);
    }

    @Test
    void getHistory_desc() {
        Page<LoyaltyTransaction> p = new PageImpl<>(List.of(LoyaltyTransaction.builder().id("tx1").build()));
        when(repository.findTransactionsByUserId(any(), any(Pageable.class))).thenReturn(p);
        assertThat(useCase.getHistory("u1", 0, 10, "createdAt", false).content()).hasSize(1);
    }
}
