package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transacción de gift card")
public class GiftCardTransactionDtoOut {
    private String id;
    private String giftCardId;
    private GiftCardTransactionType type;
    private BigDecimal amount;
    private String orderId;
    private Instant createdAt;
}
