package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import com.backandwhite.domain.valueobject.LoyaltyAction;
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
@Table(name = "loyalty_rules")
public class LoyaltyRuleEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private LoyaltyAction action;

    @Column(name = "points_per_unit")
    private int pointsPerUnit;

    @Column
    private Boolean active;
}
