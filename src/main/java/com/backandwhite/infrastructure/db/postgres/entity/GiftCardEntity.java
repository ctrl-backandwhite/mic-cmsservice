package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import com.backandwhite.common.domain.valueobject.MoneyConverter;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;

@With
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_cards")
public class GiftCardEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "design_id", length = 64)
    private String designId;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "original_amount", precision = 12, scale = 2)
    private Money originalAmount;

    @Convert(converter = MoneyConverter.class)
    @Column(precision = 12, scale = 2)
    private Money balance;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GiftCardStatus status;

    @Column(name = "buyer_id", length = 64)
    private String buyerId;

    @Column(name = "recipient_name", length = 200)
    private String recipientName;

    @Column(name = "recipient_email", length = 255)
    private String recipientEmail;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "activated_at")
    private Instant activatedAt;
}
