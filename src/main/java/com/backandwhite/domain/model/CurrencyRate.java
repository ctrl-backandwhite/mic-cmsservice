package com.backandwhite.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
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
