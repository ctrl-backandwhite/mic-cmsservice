package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.FlowType;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flow {
    private String id;
    private String name;
    private FlowType type;
    private boolean active;
    private List<FlowStep> steps;
    private Instant createdAt;
    private Instant updatedAt;
}
