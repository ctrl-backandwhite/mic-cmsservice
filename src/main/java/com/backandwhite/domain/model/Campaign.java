package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.CampaignType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    private String id;
    private String name;
    private CampaignType type;
    private BigDecimal value;
    private String badge;
    private Instant startDate;
    private Instant endDate;
    private List<String> appliesToCategories;
    private List<String> appliesToProducts;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
