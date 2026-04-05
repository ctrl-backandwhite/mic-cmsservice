package com.backandwhite.domain.model;

import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowStep {
    private String id;
    private String flowId;
    private int position;
    private String title;
    private String description;
    private String icon;
    private Integer slaDays;
    private String triggerType;
    private boolean sendEmail;
    private boolean sendSms;
    private Instant createdAt;
    private Instant updatedAt;
}
