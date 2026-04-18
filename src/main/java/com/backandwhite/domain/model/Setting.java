package com.backandwhite.domain.model;

import com.backandwhite.domain.valueobject.SettingSection;
import java.time.Instant;
import java.util.Map;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    private String key;
    private Map<String, Object> value;
    private SettingSection section;
    private Instant createdAt;
    private Instant updatedAt;
}
