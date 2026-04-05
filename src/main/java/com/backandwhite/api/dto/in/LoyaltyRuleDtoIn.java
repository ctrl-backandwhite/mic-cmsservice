package com.backandwhite.api.dto.in;

import com.backandwhite.domain.valueobject.LoyaltyAction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar regla de fidelización")
public class LoyaltyRuleDtoIn {
    @NotNull
    private LoyaltyAction action;
    @NotNull
    private Integer pointsPerUnit;
    private boolean active;
}
