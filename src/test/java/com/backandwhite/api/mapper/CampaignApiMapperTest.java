package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.CampaignDtoIn;
import com.backandwhite.api.dto.out.CampaignDtoOut;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.valueobject.CampaignType;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CampaignApiMapperTest {
    private final CampaignApiMapper mapper = new CampaignApiMapperImpl();

    @Test
    void nullsReturnNull() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
        assertThat(mapper.moneyToBigDecimal(null)).isNull();
        assertThat(mapper.bigDecimalToMoney(null)).isNull();
    }

    @Test
    void toDto_copiesFields() {
        Campaign c = Campaign.builder().id("c1").name("c").type(CampaignType.PERCENTAGE)
                .value(Money.of(new BigDecimal("10"))).active(true).build();
        CampaignDtoOut dto = mapper.toDto(c);
        assertThat(dto.getId()).isEqualTo("c1");
        assertThat(dto.getValue()).isEqualByComparingTo("10");
    }

    @Test
    void toDomain_copiesFields() {
        CampaignDtoIn in = CampaignDtoIn.builder().name("c").type(CampaignType.PERCENTAGE).value(new BigDecimal("10"))
                .startDate(Instant.EPOCH).endDate(Instant.EPOCH).build();
        Campaign c = mapper.toDomain(in);
        assertThat(c.getId()).isNull();
        assertThat(c.getValue().getAmount()).isEqualByComparingTo("10");
    }

    @Test
    void moneyToBigDecimal_nonNull() {
        assertThat(mapper.moneyToBigDecimal(Money.of(new BigDecimal("10")))).isEqualByComparingTo("10");
    }

    @Test
    void bigDecimalToMoney_nonNull() {
        assertThat(mapper.bigDecimalToMoney(new BigDecimal("5")).getAmount()).isEqualByComparingTo("5");
    }
}
