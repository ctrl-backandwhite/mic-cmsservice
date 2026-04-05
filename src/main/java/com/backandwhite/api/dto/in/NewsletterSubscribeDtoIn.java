package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para suscripción al newsletter")
public class NewsletterSubscribeDtoIn {
    @NotBlank
    @Email
    private String email;
    private String source;
}
