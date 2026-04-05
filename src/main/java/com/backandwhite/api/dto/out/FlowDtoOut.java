package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.FlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Flujo operacional")
public class FlowDtoOut {
    private String id;
    private String name;
    private FlowType type;
    private boolean active;
    private List<FlowStepDtoOut> steps;
    private Instant createdAt;
    private Instant updatedAt;
}
