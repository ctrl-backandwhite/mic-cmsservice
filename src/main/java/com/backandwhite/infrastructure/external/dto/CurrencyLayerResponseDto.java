package com.backandwhite.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Typed HTTP response for the CurrencyLayer {@code /live} endpoint.
 *
 * <pre>
 * { "success": true,
 *   "terms": "...", "privacy": "...",
 *   "timestamp": 1725360000,
 *   "source": "USD",
 *   "quotes": { "USDEUR": 0.925, "USDGBP": 0.781, ... } }
 * </pre>
 *
 * On failure:
 *
 * <pre>
 * { "success": false,
 *   "error": { "code": 101, "type": "invalid_access_key", "info": "..." } }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyLayerResponseDto(Boolean success, Long timestamp, String source, Map<String, BigDecimal> quotes,
        CurrencyLayerErrorDto error) {
}
