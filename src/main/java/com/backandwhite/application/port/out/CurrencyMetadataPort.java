package com.backandwhite.application.port.out;

import com.backandwhite.domain.valueobject.CurrencyMeta;

/**
 * Port that exposes descriptive metadata (name, symbol, country, timezone,
 * language, flag) for a currency code. Adapters may resolve this from static
 * tables, YAML, or a remote service.
 */
public interface CurrencyMetadataPort {

    /**
     * Returns metadata for the given currency code, or a sensible default when the
     * code is unknown. Never returns {@code null}.
     */
    CurrencyMeta getOrDefault(String currencyCode);
}
