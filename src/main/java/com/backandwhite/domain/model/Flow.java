package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.FlowType;
import java.time.Instant;
import java.util.List;
import lombok.*;

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
