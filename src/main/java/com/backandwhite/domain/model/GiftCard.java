package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.GiftCardStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCard {
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
    private Instant createdAt;
    private Instant updatedAt;
}
