package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Toggle currency rate active status")
public class CurrencyRateToggleDtoIn {
    @NotNull
    private Boolean active;
}
