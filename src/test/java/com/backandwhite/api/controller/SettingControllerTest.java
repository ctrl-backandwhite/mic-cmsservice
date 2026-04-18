package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.SettingDtoIn;
import com.backandwhite.api.dto.out.SettingDtoOut;
import com.backandwhite.api.mapper.SettingApiMapper;
import com.backandwhite.application.usecase.SettingUseCase;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingControllerTest {
    @Mock
    SettingUseCase useCase;
    @Mock
    SettingApiMapper mapper;
    @InjectMocks
    SettingController controller;

    @Test
    void findAll_ok() {
        when(useCase.findAll()).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAll("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findBySection_ok() {
        when(useCase.findBySection(SettingSection.GENERAL)).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findBySection("tok", SettingSection.GENERAL).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findByKey_ok() {
        Setting s = Setting.builder().key("k").build();
        when(useCase.findByKey("k")).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(SettingDtoOut.builder().key("k").build());
        assertThat(controller.findByKey("tok", "k").getBody().getKey()).isEqualTo("k");
    }

    @Test
    void save_ok() {
        SettingDtoIn in = SettingDtoIn.builder().key("k").value(Map.of()).section(SettingSection.GENERAL).build();
        Setting s = Setting.builder().build();
        when(mapper.toDomain(any())).thenReturn(s);
        when(useCase.save(s)).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(SettingDtoOut.builder().build());
        assertThat(controller.save("tok", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("tok", "k").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("k");
    }
}
