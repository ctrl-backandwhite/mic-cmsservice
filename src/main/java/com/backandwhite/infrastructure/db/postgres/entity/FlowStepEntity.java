package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.infrastructure.entity.AuditableEntity;
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
@Table(name = "flow_steps")
public class FlowStepEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "flow_id", nullable = false, length = 64)
    private String flowId;

    @Column
    private Integer position;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String icon;

    @Column(name = "sla_days")
    private Integer slaDays;

    @Column(name = "trigger_type", length = 50)
    private String triggerType;

    @Column(name = "send_email")
    private Boolean sendEmail;

    @Column(name = "send_sms")
    private Boolean sendSms;
}
