package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tier de fidelización")
public class LoyaltyTierDtoOut {
    private String id;
    private String name;
    private int minPoints;
    private int maxPoints;
    private BigDecimal multiplier;
    private List<Map<String, Object>> benefits;
    private Instant createdAt;
    private Instant updatedAt;
}
