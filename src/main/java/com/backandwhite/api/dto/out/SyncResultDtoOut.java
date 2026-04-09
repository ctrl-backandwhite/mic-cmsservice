package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Currency sync result")
public class SyncResultDtoOut {
    private int ratesUpdated;
    private Instant syncedAt;
}
