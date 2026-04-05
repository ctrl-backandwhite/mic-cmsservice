package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.NewsletterStatus;
import lombok.*;

import java.time.Instant;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriber {
    private String id;
    private String email;
    private NewsletterStatus status;
    private Instant subscribedAt;
    private Instant unsubscribedAt;
    private String source;
    private Instant createdAt;
    private Instant updatedAt;
}
