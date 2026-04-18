package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;
import lombok.experimental.SuperBuilder;

@With
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_rates")
public class CurrencyRateEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "currency_code", nullable = false, unique = true, length = 3)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false, length = 100)
    private String currencyName;

    @Column(name = "currency_symbol", nullable = false, length = 10)
    private String currencySymbol;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "country_code", nullable = false, length = 5)
    private String countryCode;

    @Column(name = "flag_emoji", nullable = false, length = 10)
    private String flagEmoji;

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;

    @Column(name = "language", nullable = false, length = 5)
    private String language;

    @Column(name = "rate", nullable = false, precision = 18, scale = 8)
    private BigDecimal rate;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_synced_at", nullable = false)
    private Instant lastSyncedAt;
}
