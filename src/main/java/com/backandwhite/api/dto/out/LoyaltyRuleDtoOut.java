package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.LoyaltyAction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Regla de fidelización")
public class LoyaltyRuleDtoOut {
    private String id;
    private LoyaltyAction action;
    private int pointsPerUnit;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
