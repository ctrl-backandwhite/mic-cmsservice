package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Diseño de gift card")
public class GiftCardDesignDtoOut {
    private String id;
    private String name;
    private Map<String, Object> gradientConfig;
    private String emoji;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
