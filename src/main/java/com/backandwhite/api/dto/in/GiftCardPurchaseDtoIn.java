package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para compra de gift card")
public class GiftCardPurchaseDtoIn {
    private String designId;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotBlank
    private String recipientName;
    @NotBlank
    private String recipientEmail;
    private String message;
    private LocalDate sendDate;
    @Schema(description = "Precise delivery instant (ISO 8601). When set in the future the scheduler holds the "
            + "Kafka purchase event until this moment arrives — overrides sendDate for timing, but sendDate is "
            + "kept as a day-level display fallback.")
    private Instant sendAt;
    private LocalDate expiryDate;
    @Schema(description = "Buyer email for guest checkout. Ignored when X-Auth-Email header is present.")
    private String buyerEmail;
}
