package com.backandwhite.domain.model;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardTransaction {
    private String id;
    private String giftCardId;
    private GiftCardTransactionType type;
    private Money amount;
    private String orderId;
    private Instant createdAt;
}
