package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.EmailTrigger;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Plantilla de email")
public class EmailTemplateDtoOut {
    private String id;
    private String name;
    private EmailTrigger triggerType;
    private String subject;
    private String bodyHtml;
    private List<String> variables;
    private String category;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
