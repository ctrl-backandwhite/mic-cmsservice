package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@With
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loyalty_transactions")
public class LoyaltyTransactionEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column
    private int points;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LoyaltyTransactionType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_id", length = 64)
    private String orderId;
}
