package com.backandwhite.domain.model;

import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeoPage {
    private String id;
    private String path;
    private String metaTitle;
    private String metaDescription;
    private boolean indexable;
    private Integer seoScore;
    private Instant createdAt;
    private Instant updatedAt;
}
