package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.SeoPageDtoIn;
import com.backandwhite.api.dto.out.SeoPageDtoOut;
import com.backandwhite.api.mapper.SeoPageApiMapper;
import com.backandwhite.application.usecase.SeoPageUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.SeoPage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeoPageControllerTest {
    @Mock
    SeoPageUseCase useCase;
    @Mock
    SeoPageApiMapper mapper;
    @InjectMocks
    SeoPageController controller;

    @Test
    void findAll_ok() {
        PageResult<SeoPage> pr = new PageResult<>(List.of(), 0, 0, 0, 20, false, false);
        when(useCase.findAll(anyInt(), anyInt(), anyString(), anyBoolean())).thenReturn(pr);
        assertThat(controller.findAll("tok", 0, 20, "path", true).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findById_ok() {
        SeoPage p = SeoPage.builder().id("p1").build();
        when(useCase.findById("p1")).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(SeoPageDtoOut.builder().id("p1").build());
        assertThat(controller.findById("tok", "p1").getBody().getId()).isEqualTo("p1");
    }

    @Test
    void findByPath_ok() {
        SeoPage p = SeoPage.builder().id("p1").path("/x").build();
        when(useCase.findByPath("/x")).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(SeoPageDtoOut.builder().path("/x").build());
        assertThat(controller.findByPath("tok", "/x").getBody().getPath()).isEqualTo("/x");
    }

    @Test
    void create_ok() {
        SeoPageDtoIn in = SeoPageDtoIn.builder().path("/x").metaTitle("T").build();
        SeoPage p = SeoPage.builder().build();
        when(mapper.toDomain(any())).thenReturn(p);
        when(useCase.create(p)).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(SeoPageDtoOut.builder().build());
        assertThat(controller.create("tok", in).getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void update_ok() {
        SeoPageDtoIn in = SeoPageDtoIn.builder().path("/x").metaTitle("T").build();
        SeoPage p = SeoPage.builder().build();
        when(mapper.toDomain(any())).thenReturn(p);
        when(useCase.update(eq("p1"), any())).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(SeoPageDtoOut.builder().build());
        assertThat(controller.update("tok", "p1", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("tok", "p1").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("p1");
    }
}
