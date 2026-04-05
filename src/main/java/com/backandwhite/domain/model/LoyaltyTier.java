package com.backandwhite.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTier {
    private String id;
    private String name;
    private int minPoints;
    private Integer maxPoints;
    private BigDecimal multiplier;
    private List<Map<String, Object>> benefits;
    private Instant createdAt;
    private Instant updatedAt;
}
