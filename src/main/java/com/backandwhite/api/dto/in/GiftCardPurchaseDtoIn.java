package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
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
    private LocalDate expiryDate;
}
