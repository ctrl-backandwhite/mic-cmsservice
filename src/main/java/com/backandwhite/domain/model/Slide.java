package com.backandwhite.domain.model;

import java.time.Instant;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slide {
    private String id;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String link;
    private String buttonText;
    private int position;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
