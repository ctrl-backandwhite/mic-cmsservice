package com.backandwhite.domain.valueobject;

import java.math.BigDecimal;

/**
 * Domain value object representing a single exchange rate quote against USD.
 * Returned by currency-rate providers (e.g. CurrencyLayer) and consumed by the
 * application layer without leaking HTTP-specific concerns.
 */
public record ExchangeRate(String currencyCode, BigDecimal rate) {
}
