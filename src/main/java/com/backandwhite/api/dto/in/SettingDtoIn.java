package com.backandwhite.api.dto.in;

import com.backandwhite.domain.valueobject.SettingSection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar configuración")
public class SettingDtoIn {
    @NotBlank
    private String key;
    @NotNull
    private Map<String, Object> value;
    @NotNull
    private SettingSection section;
}
