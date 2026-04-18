package com.backandwhite.api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Mensaje de contacto")
public class ContactMessageDtoOut {
    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private boolean read;
    private Instant createdAt;
    private Instant updatedAt;
}
