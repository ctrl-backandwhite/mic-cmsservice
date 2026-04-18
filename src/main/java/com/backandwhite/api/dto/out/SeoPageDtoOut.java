package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Página SEO")
public class SeoPageDtoOut {
    private String id;
    private String path;
    private String metaTitle;
    private String metaDescription;
    private boolean indexable;
    private Integer seoScore;
    private Instant createdAt;
    private Instant updatedAt;
}
