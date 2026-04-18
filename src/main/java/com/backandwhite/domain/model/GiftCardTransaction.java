package com.backandwhite.domain.model;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import java.time.Instant;
import lombok.*;

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
