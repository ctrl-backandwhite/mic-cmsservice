package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Currency rate data")
public class CurrencyRateDtoOut {
    private String id;
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
    private String countryName;
    private String countryCode;
    private String flagEmoji;
    private String timezone;
    private String language;
    private BigDecimal rate;
    private boolean active;
    private Instant lastSyncedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
