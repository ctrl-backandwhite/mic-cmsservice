package com.backandwhite.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
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

@ExtendWith(MockitoExtension.class)
class LoyaltyMachineTest {

    @Mock
    private LoyaltyRepository loyaltyRepository;

    @Mock
    private CmsEventPort cmsEventPort;

    @InjectMocks
    private LoyaltyMachine machine;

    private LoyaltyTier tier(String id, String name, int min, BigDecimal mult) {
        return LoyaltyTier.builder().id(id).name(name).minPoints(min).multiplier(mult).build();
    }

    @Test
    void processOrderDelivery_alreadyProcessed_returnsZero() {
        when(loyaltyRepository.existsEarnTransactionByOrderId("o1")).thenReturn(true);
        when(loyaltyRepository.sumPointsByUserId("u1")).thenReturn(50);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of());

        LoyaltyResult r = machine.processOrderDelivery("u1", new BigDecimal("100"), "o1");
        assertThat(r.getEarnedPoints()).isEqualTo(0);
        assertThat(r.getCurrentTier()).isEqualTo("Bronze");
        verify(loyaltyRepository, never()).saveTransaction(any());
    }

    @Test
    void processOrderDelivery_emptyPoints_returnsEmpty() {
        when(loyaltyRepository.existsEarnTransactionByOrderId("o1")).thenReturn(false);
        when(loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE))
                .thenReturn(Optional.of(LoyaltyRule.builder().pointsPerUnit(1).build()));
        when(loyaltyRepository.sumPointsByUserId("u1")).thenReturn(0);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of());

        LoyaltyResult r = machine.processOrderDelivery("u1", BigDecimal.ZERO, "o1");
        assertThat(r.getEarnedPoints()).isEqualTo(0);
        assertThat(r.isLeveledUp()).isFalse();
    }

    @Test
    void processOrderDelivery_earnsPoints_noLevelUp() {
        when(loyaltyRepository.existsEarnTransactionByOrderId(anyString())).thenReturn(false);
        when(loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE))
                .thenReturn(Optional.of(LoyaltyRule.builder().pointsPerUnit(1).build()));
        when(loyaltyRepository.sumPointsByUserId("u1")).thenReturn(50);
        LoyaltyTier bronze = tier("t1", "Bronze", 0, BigDecimal.ONE);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of(bronze));
        when(loyaltyRepository.saveTransaction(any(LoyaltyTransaction.class))).thenAnswer(i -> i.getArgument(0));

        LoyaltyResult r = machine.processOrderDelivery("u1", new BigDecimal("10"), "o1");
        assertThat(r.getEarnedPoints()).isEqualTo(10);
        assertThat(r.getBonusPoints()).isEqualTo(0);
        assertThat(r.isLeveledUp()).isFalse();
        assertThat(r.getCurrentTier()).isEqualTo("Bronze");
    }

    @Test
    void processOrderDelivery_levelsUp_addsBonus() {
        when(loyaltyRepository.existsEarnTransactionByOrderId(anyString())).thenReturn(false);
        when(loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE))
                .thenReturn(Optional.of(LoyaltyRule.builder().pointsPerUnit(1).build()));
        when(loyaltyRepository.sumPointsByUserId("u1")).thenReturn(40);
        LoyaltyTier bronze = tier("t1", "Bronze", 0, BigDecimal.ONE);
        LoyaltyTier silver = tier("t2", "Silver", 100, BigDecimal.ONE);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of(bronze, silver));
        when(loyaltyRepository.saveTransaction(any(LoyaltyTransaction.class))).thenAnswer(i -> i.getArgument(0));

        LoyaltyResult r = machine.processOrderDelivery("u1", new BigDecimal("100"), "o1");
        assertThat(r.getEarnedPoints()).isEqualTo(100);
        assertThat(r.getBonusPoints()).isEqualTo(100);
        assertThat(r.isLeveledUp()).isTrue();
        assertThat(r.getCurrentTier()).isEqualTo("Silver");
        verify(cmsEventPort).publishLoyaltyPointsEarned(eq("u1"), eq("o1"), anyInt(), anyInt(), anyString());
    }

    @Test
    void processOrderDelivery_noTiers_returnsBronze() {
        when(loyaltyRepository.existsEarnTransactionByOrderId(anyString())).thenReturn(false);
        when(loyaltyRepository.findActiveRuleByAction(LoyaltyAction.PURCHASE)).thenReturn(Optional.empty());
        when(loyaltyRepository.sumPointsByUserId("u1")).thenReturn(0);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of());
        when(loyaltyRepository.saveTransaction(any(LoyaltyTransaction.class))).thenAnswer(i -> i.getArgument(0));

        LoyaltyResult r = machine.processOrderDelivery("u1", new BigDecimal("50"), "o1");
        assertThat(r.getEarnedPoints()).isEqualTo(50);
        assertThat(r.getCurrentTier()).isEqualTo("Bronze");
    }

    @Test
    void resolveTier_belowFirstTier_returnsNull() {
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of(tier("t1", "Bronze", 100, BigDecimal.ONE)));
        assertThat(machine.resolveTier(50)).isNull();
    }

    @Test
    void resolveTier_matchesHighestTier() {
        LoyaltyTier bronze = tier("t1", "Bronze", 0, BigDecimal.ONE);
        LoyaltyTier silver = tier("t2", "Silver", 100, BigDecimal.ONE);
        LoyaltyTier gold = tier("t3", "Gold", 500, BigDecimal.ONE);
        when(loyaltyRepository.findAllTiers()).thenReturn(List.of(bronze, silver, gold));

        assertThat(machine.resolveTier(300).getName()).isEqualTo("Silver");
        assertThat(machine.resolveTier(1000).getName()).isEqualTo("Gold");
    }

    @Test
    void loyaltyResult_empty_buildsEmpty() {
        LoyaltyResult empty = LoyaltyResult.empty();
        assertThat(empty.getEarnedPoints()).isZero();
        assertThat(empty.getBonusPoints()).isZero();
        assertThat(empty.isLeveledUp()).isFalse();
    }

    @Test
    void loyaltyResult_allArgsAndNoArgs_areAvailable() {
        LoyaltyResult r = new LoyaltyResult(10, 5, 100, "Bronze", "Silver", true);
        assertThat(r.getEarnedPoints()).isEqualTo(10);
        LoyaltyResult blank = new LoyaltyResult();
        blank.setCurrentTier("X");
        assertThat(blank.getCurrentTier()).isEqualTo("X");
    }
}
