package com.backandwhite.api.dto.out;

import com.backandwhite.domain.valueobject.NewsletterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Suscriptor del newsletter")
public class NewsletterSubscriberDtoOut {
    private String id;
    private String email;
    private NewsletterStatus status;
    private Instant subscribedAt;
    private Instant unsubscribedAt;
    private String source;
    private Instant createdAt;
    private Instant updatedAt;
}
