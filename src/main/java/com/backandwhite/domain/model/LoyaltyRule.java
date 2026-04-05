package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.LoyaltyAction;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyRule {
    private String id;
    private LoyaltyAction action;
    private int pointsPerUnit;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
