package com.backandwhite.domain.model;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCard {
    private String id;
    private String code;
    private String designId;
    private Money originalAmount;
    private Money balance;
    private GiftCardStatus status;
    private String buyerId;
    private String buyerEmail;
    private String recipientName;
    private String recipientEmail;
    private String message;
    private LocalDate sendDate;
    /**
     * Precise delivery instant. When set in the future, the scheduler holds the
     * Kafka purchase event until this moment is reached. {@code sendDate} is kept
     * as a day-level legacy field for display; {@code sendAt} drives scheduling.
     */
    private Instant sendAt;
    private LocalDate expiryDate;
    private Instant activatedAt;
    /**
     * True once the {@code GiftCardPurchasedEvent} has been published (so the
     * recipient email + fiscal invoice fire). Scheduled purchases start with this
     * set to {@code false}; the {@code GiftCardScheduledSender} job flips it once
     * {@code sendDate} is reached.
     */
    private boolean emailSent;
    private Instant createdAt;
    private Instant updatedAt;
}
