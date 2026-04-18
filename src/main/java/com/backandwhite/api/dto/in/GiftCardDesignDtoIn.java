package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar diseño de gift card")
public class GiftCardDesignDtoIn {
    @NotBlank
    private String name;
    private Map<String, Object> gradientConfig;
    private String emoji;
    private boolean active;
}
