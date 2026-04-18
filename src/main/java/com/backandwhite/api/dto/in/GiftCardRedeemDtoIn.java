package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para canjear gift card")
public class GiftCardRedeemDtoIn {
    @NotBlank
    private String code;
    @NotNull
    @Positive
    private BigDecimal amount;
    private String orderId;
}
