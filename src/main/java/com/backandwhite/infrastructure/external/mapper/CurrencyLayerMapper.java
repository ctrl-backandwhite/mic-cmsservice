package com.backandwhite.infrastructure.external.mapper;

import com.backandwhite.domain.valueobject.ExchangeRate;
import com.backandwhite.infrastructure.external.dto.CurrencyLayerResponseDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;

/**
 * Converts the CurrencyLayer {@link CurrencyLayerResponseDto} (HTTP payload)
 * into a list of domain {@link ExchangeRate} values. Keys in the upstream
 * payload are formatted as {@code USDEUR}, {@code USDGBP}, etc. — this mapper
 * strips the {@code USD} prefix and normalizes the output.
 */
@Mapper(componentModel = "spring")
public interface CurrencyLayerMapper {

    /**
     * Map the full response DTO to the list of domain exchange rates.
     *
     * @return empty list when the DTO is null, unsuccessful, or has no quotes.
     */
    default List<ExchangeRate> toExchangeRates(CurrencyLayerResponseDto dto) {
        if (dto == null || !Boolean.TRUE.equals(dto.success()) || dto.quotes() == null || dto.quotes().isEmpty()) {
            return Collections.emptyList();
        }
        List<ExchangeRate> rates = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : dto.quotes().entrySet()) {
            ExchangeRate rate = toExchangeRate(entry.getKey(), entry.getValue());
            if (rate != null) {
                rates.add(rate);
            }
        }
        return rates;
    }

    /**
     * Map one quote entry. Returns {@code null} when the key doesn't match the
     * expected {@code USDXXX} pattern or the rate is missing.
     */
    default ExchangeRate toExchangeRate(String quoteKey, BigDecimal value) {
        if (quoteKey == null || value == null || quoteKey.length() != 6 || !quoteKey.startsWith("USD")) {
            return null;
        }
        return new ExchangeRate(quoteKey.substring(3), value);
    }
}
