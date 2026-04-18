package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.LoyaltyRuleDtoIn;
import com.backandwhite.api.dto.in.LoyaltyTierDtoIn;
import com.backandwhite.api.dto.out.LoyaltyRuleDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTierDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTransactionDtoOut;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class LoyaltyApiMapperTest {
    private final LoyaltyApiMapper mapper = new LoyaltyApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toTierDto(null)).isNull();
        assertThat(mapper.toTierDtoList(null)).isNull();
        assertThat(mapper.toTierDomain(null)).isNull();
        assertThat(mapper.toRuleDto(null)).isNull();
        assertThat(mapper.toRuleDtoList(null)).isNull();
        assertThat(mapper.toRuleDomain(null)).isNull();
        assertThat(mapper.toTransactionDto(null)).isNull();
        assertThat(mapper.toTransactionDtoList(null)).isNull();
    }

    @Test
    void toTierDto_copies() {
        LoyaltyTier t = LoyaltyTier.builder().id("t1").name("Bronze").minPoints(0).maxPoints(100)
                .multiplier(BigDecimal.ONE).build();
        LoyaltyTierDtoOut out = mapper.toTierDto(t);
        assertThat(out.getId()).isEqualTo("t1");
    }

    @Test
    void toTierDtoList() {
        assertThat(mapper.toTierDtoList(List.of(LoyaltyTier.builder().id("t1").build()))).hasSize(1);
    }

    @Test
    void toTierDomain_ignoresId() {
        LoyaltyTierDtoIn in = LoyaltyTierDtoIn.builder().name("Bronze").minPoints(0).maxPoints(100)
                .multiplier(BigDecimal.ONE).build();
        LoyaltyTier t = mapper.toTierDomain(in);
        assertThat(t.getId()).isNull();
    }

    @Test
    void toRuleDto_copies() {
        LoyaltyRule r = LoyaltyRule.builder().id("r1").action(LoyaltyAction.PURCHASE).pointsPerUnit(1).build();
        LoyaltyRuleDtoOut out = mapper.toRuleDto(r);
        assertThat(out.getId()).isEqualTo("r1");
    }

    @Test
    void toRuleDtoList() {
        assertThat(mapper.toRuleDtoList(List.of(LoyaltyRule.builder().id("r1").build()))).hasSize(1);
    }

    @Test
    void toRuleDomain_ignoresId() {
        LoyaltyRuleDtoIn in = LoyaltyRuleDtoIn.builder().action(LoyaltyAction.PURCHASE).pointsPerUnit(1).build();
        LoyaltyRule r = mapper.toRuleDomain(in);
        assertThat(r.getId()).isNull();
    }

    @Test
    void toTransactionDto_copies() {
        LoyaltyTransaction tx = LoyaltyTransaction.builder().id("tx1").userId("u1").points(10)
                .type(LoyaltyTransactionType.EARN).build();
        LoyaltyTransactionDtoOut out = mapper.toTransactionDto(tx);
        assertThat(out.getId()).isEqualTo("tx1");
    }

    @Test
    void toTransactionDtoList() {
        assertThat(mapper.toTransactionDtoList(List.of(LoyaltyTransaction.builder().id("tx1").build()))).hasSize(1);
    }
}
