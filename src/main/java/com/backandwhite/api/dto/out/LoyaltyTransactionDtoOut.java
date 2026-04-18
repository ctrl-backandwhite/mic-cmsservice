package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transacción de fidelización")
public class LoyaltyTransactionDtoOut {
    private String id;
    private String userId;
    private int points;
    private LoyaltyTransactionType type;
    private String description;
    private String orderId;
    private Instant createdAt;
}
