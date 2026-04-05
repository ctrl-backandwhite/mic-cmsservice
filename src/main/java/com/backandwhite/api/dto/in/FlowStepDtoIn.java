package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para paso de flujo")
public class FlowStepDtoIn {
    @NotBlank
    private String title;
    private String description;
    private String icon;
    private Integer slaDays;
    private String triggerType;
    private boolean sendEmail;
    private boolean sendSms;
}
