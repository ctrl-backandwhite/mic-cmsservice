package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paso de flujo")
public class FlowStepDtoOut {
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
