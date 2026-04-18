package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.LoyaltyEarnDtoIn;
import com.backandwhite.api.dto.in.LoyaltyRuleDtoIn;
import com.backandwhite.api.dto.in.LoyaltyTierDtoIn;
import com.backandwhite.api.dto.out.LoyaltyRuleDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTierDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTransactionDtoOut;
import com.backandwhite.api.mapper.LoyaltyApiMapper;
import com.backandwhite.application.usecase.LoyaltyUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoyaltyControllerTest {
    @Mock
    LoyaltyUseCase useCase;
    @Mock
    LoyaltyApiMapper mapper;
    @InjectMocks
    LoyaltyController controller;

    @Test
    void findAllTiers_ok() {
        when(useCase.findAllTiers()).thenReturn(List.of());
        when(mapper.toTierDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAllTiers("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findTierById_ok() {
        LoyaltyTier t = LoyaltyTier.builder().id("t1").build();
        when(useCase.findTierById("t1")).thenReturn(t);
        when(mapper.toTierDto(t)).thenReturn(LoyaltyTierDtoOut.builder().id("t1").build());
        assertThat(controller.findTierById("tok", "t1").getBody().getId()).isEqualTo("t1");
    }

    @Test
    void createTier_ok() {
        LoyaltyTierDtoIn in = LoyaltyTierDtoIn.builder().name("n").minPoints(0).maxPoints(100)
                .multiplier(BigDecimal.ONE).build();
        LoyaltyTier t = LoyaltyTier.builder().build();
        when(mapper.toTierDomain(any())).thenReturn(t);
        when(useCase.createTier(t)).thenReturn(t);
        when(mapper.toTierDto(t)).thenReturn(LoyaltyTierDtoOut.builder().build());
        assertThat(controller.createTier("tok", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void updateTier_ok() {
        LoyaltyTierDtoIn in = LoyaltyTierDtoIn.builder().name("n").minPoints(0).maxPoints(100)
                .multiplier(BigDecimal.ONE).build();
        LoyaltyTier t = LoyaltyTier.builder().build();
        when(mapper.toTierDomain(any())).thenReturn(t);
        when(useCase.updateTier(eq("t1"), any())).thenReturn(t);
        when(mapper.toTierDto(t)).thenReturn(LoyaltyTierDtoOut.builder().build());
        assertThat(controller.updateTier("tok", "t1", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void deleteTier_ok() {
        assertThat(controller.deleteTier("tok", "t1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).deleteTier("t1");
    }

    @Test
    void findAllRules_ok() {
        when(useCase.findAllRules()).thenReturn(List.of());
        when(mapper.toRuleDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAllRules("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findRuleById_ok() {
        LoyaltyRule r = LoyaltyRule.builder().id("r1").build();
        when(useCase.findRuleById("r1")).thenReturn(r);
        when(mapper.toRuleDto(r)).thenReturn(LoyaltyRuleDtoOut.builder().id("r1").build());
        assertThat(controller.findRuleById("tok", "r1").getBody().getId()).isEqualTo("r1");
    }

    @Test
    void createRule_ok() {
        LoyaltyRuleDtoIn in = LoyaltyRuleDtoIn.builder().action(LoyaltyAction.PURCHASE).pointsPerUnit(1).build();
        LoyaltyRule r = LoyaltyRule.builder().build();
        when(mapper.toRuleDomain(any())).thenReturn(r);
        when(useCase.createRule(r)).thenReturn(r);
        when(mapper.toRuleDto(r)).thenReturn(LoyaltyRuleDtoOut.builder().build());
        assertThat(controller.createRule("tok", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void updateRule_ok() {
        LoyaltyRuleDtoIn in = LoyaltyRuleDtoIn.builder().action(LoyaltyAction.PURCHASE).pointsPerUnit(1).build();
        LoyaltyRule r = LoyaltyRule.builder().build();
        when(mapper.toRuleDomain(any())).thenReturn(r);
        when(useCase.updateRule(eq("r1"), any())).thenReturn(r);
        when(mapper.toRuleDto(r)).thenReturn(LoyaltyRuleDtoOut.builder().build());
        assertThat(controller.updateRule("tok", "r1", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void deleteRule_ok() {
        assertThat(controller.deleteRule("tok", "r1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).deleteRule("r1");
    }

    @Test
    void getBalance_ok() {
        when(useCase.getBalance("u1")).thenReturn(50);
        assertThat(controller.getBalance("tok", "u1").getBody().getBalance()).isEqualTo(50);
    }

    @Test
    void earnPoints_ok() {
        LoyaltyEarnDtoIn in = LoyaltyEarnDtoIn.builder().points(10).description("d").orderId("o").build();
        LoyaltyTransaction tx = LoyaltyTransaction.builder().build();
        when(useCase.earnPoints("u1", 10, "d", "o")).thenReturn(tx);
        when(mapper.toTransactionDto(tx)).thenReturn(LoyaltyTransactionDtoOut.builder().build());
        assertThat(controller.earnPoints("tok", "u1", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void redeemPoints_ok() {
        LoyaltyEarnDtoIn in = LoyaltyEarnDtoIn.builder().points(10).description("d").orderId("o").build();
        LoyaltyTransaction tx = LoyaltyTransaction.builder().build();
        when(useCase.redeemPoints("u1", 10, "d", "o")).thenReturn(tx);
        when(mapper.toTransactionDto(tx)).thenReturn(LoyaltyTransactionDtoOut.builder().build());
        assertThat(controller.redeemPoints("tok", "u1", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void getRedemptionRate_withActiveRule() {
        LoyaltyRule r = LoyaltyRule.builder().action(LoyaltyAction.REDEMPTION).pointsPerUnit(50).active(true).build();
        when(useCase.findAllRules()).thenReturn(List.of(r));
        assertThat(controller.getRedemptionRate("tok").getBody().get("pointsPerDollar")).isEqualTo(50);
    }

    @Test
    void getRedemptionRate_defaults() {
        when(useCase.findAllRules()).thenReturn(List.of());
        assertThat(controller.getRedemptionRate("tok").getBody().get("pointsPerDollar")).isEqualTo(100);
    }

    @Test
    void getHistory_ok() {
        PageResult<LoyaltyTransaction> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.getHistory(anyString(), anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.getHistory("tok", "u1", 0, 20, "createdAt", false).getStatusCode().value())
                .isEqualTo(200);
    }
}
