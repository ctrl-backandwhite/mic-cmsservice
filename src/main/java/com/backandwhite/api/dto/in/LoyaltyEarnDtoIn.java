package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para otorgar/canjear puntos")
public class LoyaltyEarnDtoIn {
    @Positive
    private int points;
    @NotBlank
    private String description;
    private String orderId;
}
