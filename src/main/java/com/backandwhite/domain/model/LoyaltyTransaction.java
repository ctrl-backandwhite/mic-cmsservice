package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransaction {
    private String id;
    private String userId;
    private int points;
    private LoyaltyTransactionType type;
    private String description;
    private String orderId;
    private Instant createdAt;
}
