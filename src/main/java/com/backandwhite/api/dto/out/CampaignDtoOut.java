package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.CampaignType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Campaña de descuento")
public class CampaignDtoOut {
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
