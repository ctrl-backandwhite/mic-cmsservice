package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.out.CurrencyRateDtoOut;
import com.backandwhite.domain.model.CurrencyRate;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CurrencyRateApiMapperTest {
    private final CurrencyRateApiMapper mapper = new CurrencyRateApiMapperImpl();

    @Test
    void toDto_null() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
    }

    @Test
    void toDto_copies() {
        CurrencyRate r = CurrencyRate.builder().id("r1").currencyCode("USD").rate(BigDecimal.ONE).active(true).build();
        CurrencyRateDtoOut dto = mapper.toDto(r);
        assertThat(dto.getCurrencyCode()).isEqualTo("USD");
        assertThat(dto.getRate()).isEqualByComparingTo("1");
    }

    @Test
    void toDtoList_mapsAll() {
        CurrencyRate r = CurrencyRate.builder().id("r1").currencyCode("USD").build();
        List<CurrencyRateDtoOut> list = mapper.toDtoList(List.of(r));
        assertThat(list).hasSize(1);
    }
}
