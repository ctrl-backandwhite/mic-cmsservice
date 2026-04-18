package com.backandwhite.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Typed node of the catalog category tree returned by {@code GET
 * /api/v1/categories}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CategoryNodeDto(String id, String name, List<CategoryNodeDto> subCategories) {
}
