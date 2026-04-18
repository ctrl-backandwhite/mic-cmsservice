package com.backandwhite.domain.valueobject;

/**
 * Domain value object with descriptive metadata for a currency code. Kept in
 * the domain so ports (e.g. {@code CurrencyMetadataPort}) expose a clean
 * contract independent of the underlying static/config-based source.
 */
public record CurrencyMeta(String currencyName, String currencySymbol, String countryName, String countryCode,
        String flagEmoji, String timezone, String language) {
}
