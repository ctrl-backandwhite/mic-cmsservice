package com.backandwhite.domain.model;

import lombok.*;

import java.time.Instant;

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
