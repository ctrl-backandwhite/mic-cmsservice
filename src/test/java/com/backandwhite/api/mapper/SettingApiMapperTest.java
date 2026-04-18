package com.backandwhite.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.api.dto.in.SettingDtoIn;
import com.backandwhite.api.dto.out.SettingDtoOut;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SettingApiMapperTest {
    private final SettingApiMapper mapper = new SettingApiMapperImpl();

    @Test
    void nulls() {
        assertThat(mapper.toDto(null)).isNull();
        assertThat(mapper.toDtoList(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDto_copies() {
        Setting s = Setting.builder().key("k").value(Map.of("a", 1)).section(SettingSection.GENERAL).build();
        SettingDtoOut out = mapper.toDto(s);
        assertThat(out.getKey()).isEqualTo("k");
    }

    @Test
    void toDtoList() {
        assertThat(mapper.toDtoList(List.of(Setting.builder().key("k").build()))).hasSize(1);
    }

    @Test
    void toDomain_copies() {
        SettingDtoIn in = SettingDtoIn.builder().key("k").value(Map.of("a", 1)).section(SettingSection.GENERAL).build();
        Setting s = mapper.toDomain(in);
        assertThat(s.getKey()).isEqualTo("k");
    }
}
