package com.backandwhite.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Minimal product projection consumed by CMS from the catalog service (only the
 * fields needed for campaign overlap validation).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductDto(String id, String categoryId) {
}
