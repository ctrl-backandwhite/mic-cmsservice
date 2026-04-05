package com.backandwhite.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardDesign {
    private String id;
    private String name;
    private Map<String, Object> gradientConfig;
    private String emoji;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
