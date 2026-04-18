package com.backandwhite.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Error payload returned by CurrencyLayer when {@code success=false}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyLayerErrorDto(Integer code, String type, String info) {
}
