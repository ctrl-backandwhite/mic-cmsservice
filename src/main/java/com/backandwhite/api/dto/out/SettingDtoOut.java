package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.SettingSection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.Instant;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Configuración del sistema")
public class SettingDtoOut {
    private String key;
    private Map<String, Object> value;
    private SettingSection section;
    private Instant createdAt;
    private Instant updatedAt;
}
