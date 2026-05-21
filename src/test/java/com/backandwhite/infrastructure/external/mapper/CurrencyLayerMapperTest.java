package com.backandwhite.infrastructure.external.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.domain.valueobject.ExchangeRate;
import com.backandwhite.infrastructure.external.dto.CurrencyLayerResponseDto;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrencyLayerMapperTest {

    private CurrencyLayerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CurrencyLayerMapperImpl();
    }

    @Test
    void toExchangeRates_nullDto_returnsEmptyList() {
        assertThat(mapper.toExchangeRates(null)).isEmpty();
    }

    @Test
    void toExchangeRates_unsuccessfulDto_returnsEmptyList() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(false, 1L, "USD",
                Map.of("USDEUR", new BigDecimal("0.92")), null);
        assertThat(mapper.toExchangeRates(dto)).isEmpty();
    }

    @Test
    void toExchangeRates_nullSuccess_returnsEmptyList() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(null, 1L, "USD",
                Map.of("USDEUR", new BigDecimal("0.92")), null);
        assertThat(mapper.toExchangeRates(dto)).isEmpty();
    }

    @Test
    void toExchangeRates_nullQuotes_returnsEmptyList() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(true, 1L, "USD", null, null);
        assertThat(mapper.toExchangeRates(dto)).isEmpty();
    }

    @Test
    void toExchangeRates_emptyQuotes_returnsEmptyList() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(true, 1L, "USD", new HashMap<>(), null);
        assertThat(mapper.toExchangeRates(dto)).isEmpty();
    }

    @Test
    void toExchangeRates_validQuotes_mapsAndStripsUsdPrefix() {
        Map<String, BigDecimal> quotes = new LinkedHashMap<>();
        quotes.put("USDEUR", new BigDecimal("0.92"));
        quotes.put("USDGBP", new BigDecimal("0.78"));
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(true, 1L, "USD", quotes, null);

        List<ExchangeRate> result = mapper.toExchangeRates(dto);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ExchangeRate::currencyCode).containsExactlyInAnyOrder("EUR", "GBP");
    }

    @Test
    void toExchangeRates_skipsBadKeysAndNullValues() {
        Map<String, BigDecimal> quotes = new LinkedHashMap<>();
        quotes.put("USDEUR", new BigDecimal("0.92"));
        quotes.put("EUR", new BigDecimal("0.92")); // bad length
        quotes.put("EURUSD", new BigDecimal("0.92")); // bad prefix
        quotes.put("USDXXX", null); // null rate
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(true, 1L, "USD", quotes, null);

        List<ExchangeRate> result = mapper.toExchangeRates(dto);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).currencyCode()).isEqualTo("EUR");
    }

    @Test
    void toExchangeRate_directCalls_coverNullBranches() {
        assertThat(mapper.toExchangeRate(null, BigDecimal.ONE)).isNull();
        assertThat(mapper.toExchangeRate("USDEUR", null)).isNull();
        assertThat(mapper.toExchangeRate("EUR", BigDecimal.ONE)).isNull();
        assertThat(mapper.toExchangeRate("XYZEUR", BigDecimal.ONE)).isNull();
        ExchangeRate ok = mapper.toExchangeRate("USDBRL", new BigDecimal("5.15"));
        assertThat(ok).isNotNull();
        assertThat(ok.currencyCode()).isEqualTo("BRL");
        assertThat(ok.rate()).isEqualByComparingTo("5.15");
    }
}
