package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.EmailTrigger;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {
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
