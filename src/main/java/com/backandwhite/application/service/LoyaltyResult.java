package com.backandwhite.application.service;

import lombok.*;

/**
 * Resultado del procesamiento de loyalty para una orden.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyResult {
    private int earnedPoints;
    private int bonusPoints;
    private int totalBalance;
    private String previousTier;
    private String currentTier;
    private boolean leveledUp;

    public static LoyaltyResult empty() {
        return LoyaltyResult.builder()
                .earnedPoints(0)
                .bonusPoints(0)
                .totalBalance(0)
                .leveledUp(false)
                .build();
    }
}
