package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar página SEO")
public class SeoPageDtoIn {
    @NotBlank
    private String path;
    @NotBlank
    private String metaTitle;
    private String metaDescription;
    private boolean indexable;
    private Integer seoScore;
}
