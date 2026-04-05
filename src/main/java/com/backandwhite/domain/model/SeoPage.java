package com.backandwhite.domain.model;

import lombok.*;

import java.time.Instant;

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
