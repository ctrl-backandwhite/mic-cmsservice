package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Balance de puntos de fidelización")
public class LoyaltyBalanceDtoOut {
    private String userId;
    private int balance;
}
