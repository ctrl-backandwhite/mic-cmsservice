package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@With
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_card_transactions")
public class GiftCardTransactionEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "gift_card_id", nullable = false, length = 64)
    private String giftCardId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GiftCardTransactionType type;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "order_id", length = 64)
    private String orderId;
}
