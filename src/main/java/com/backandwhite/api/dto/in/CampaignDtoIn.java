package com.backandwhite.api.dto.in;

import com.backandwhite.domain.valueobject.CampaignType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar campaña")
public class CampaignDtoIn {
    @NotBlank
    private String name;
    @NotNull
    private CampaignType type;
    @NotNull
    private BigDecimal value;
    private String badge;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
    private List<String> appliesToCategories;
    private List<String> appliesToProducts;
    private boolean active;
}
