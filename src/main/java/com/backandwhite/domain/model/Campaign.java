package com.backandwhite.domain.model;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.valueobject.CampaignType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    private String id;
    private String name;
    private CampaignType type;
    private Money value;
    private String badge;
    private String badgeColor;
    private Instant startDate;
    private Instant endDate;
    private List<String> appliesToCategories;
    private List<String> appliesToProducts;
    private boolean active;
    private String description;
    private BigDecimal minOrder;
    private BigDecimal maxDiscount;
    private Integer buyQty;
    private Integer getQty;
    private Boolean isFlash;
    private Boolean showOnHome;
    private Integer priority;
    private Instant createdAt;
    private Instant updatedAt;
}
