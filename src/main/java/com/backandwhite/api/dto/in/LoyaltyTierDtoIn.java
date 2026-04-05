package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar tier de fidelización")
public class LoyaltyTierDtoIn {
    @NotBlank
    private String name;
    @NotNull
    private Integer minPoints;
    @NotNull
    private Integer maxPoints;
    @NotNull
    private BigDecimal multiplier;
    private List<Map<String, Object>> benefits;
}
