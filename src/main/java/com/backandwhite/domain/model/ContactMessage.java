package com.backandwhite.domain.model;

import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {
    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private boolean read;
    private Instant createdAt;
    private Instant updatedAt;
}
