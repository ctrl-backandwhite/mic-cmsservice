package com.backandwhite.api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar slide")
public class SlideDtoIn {
    @NotBlank
    private String title;
    private String subtitle;
    @NotBlank
    private String imageUrl;
    private String link;
    private String buttonText;
    private int position;
    private boolean active;
}
