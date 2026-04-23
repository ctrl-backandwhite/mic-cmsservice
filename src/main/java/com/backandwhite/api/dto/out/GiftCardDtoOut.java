package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.GiftCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Gift card")
public class GiftCardDtoOut {
    private String id;
    private String code;
    private String designId;
    private BigDecimal originalAmount;
    private BigDecimal balance;
    private GiftCardStatus status;
    private String buyerId;
    private String recipientName;
    private String recipientEmail;
    private String message;
    private LocalDate sendDate;
    private LocalDate expiryDate;
    private Instant activatedAt;
    /**
     * True once the Kafka purchase event has fired (and therefore the recipient
     * email + fiscal invoice went out). False while a scheduled card is waiting for
     * its {@code sendDate}.
     */
    private boolean emailSent;
    private Instant createdAt;
    private Instant updatedAt;
}
