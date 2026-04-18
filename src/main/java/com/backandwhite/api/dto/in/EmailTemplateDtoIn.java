package com.backandwhite.api.dto.in;

import com.backandwhite.domain.valueobject.EmailTrigger;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear/actualizar plantilla de email")
public class EmailTemplateDtoIn {
    @NotBlank
    private String name;
    @NotNull
    private EmailTrigger triggerType;
    @NotBlank
    private String subject;
    @NotBlank
    private String bodyHtml;
    private List<String> variables;
    private String category;
    private boolean active;
}
