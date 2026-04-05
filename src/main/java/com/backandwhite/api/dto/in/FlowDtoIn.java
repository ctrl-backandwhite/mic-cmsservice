package com.backandwhite.api.dto.in;

import com.backandwhite.domain.valueobject.FlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar flujo")
public class FlowDtoIn {
    @NotBlank
    private String name;
    @NotNull
    private FlowType type;
    private boolean active;
}
