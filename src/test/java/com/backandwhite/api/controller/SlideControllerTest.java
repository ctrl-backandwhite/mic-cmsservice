package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.SlideDtoIn;
import com.backandwhite.api.dto.out.SlideDtoOut;
import com.backandwhite.api.mapper.SlideApiMapper;
import com.backandwhite.application.usecase.SlideUseCase;
import com.backandwhite.domain.model.Slide;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SlideControllerTest {

    @Mock
    SlideUseCase useCase;

    @Mock
    SlideApiMapper mapper;

    @InjectMocks
    SlideController controller;

    @Test
    void findAllActive_returns200() {
        when(useCase.findAllActive("es")).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        ResponseEntity<List<SlideDtoOut>> r = controller.findAllActive("tok", "es");
        assertThat(r.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findAll_returns200() {
        when(useCase.findAll()).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAll("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findById_returns200() {
        Slide s = Slide.builder().id("1").build();
        when(useCase.findById("1")).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(SlideDtoOut.builder().id("1").build());
        assertThat(controller.findById("1", "tok").getBody().getId()).isEqualTo("1");
    }

    @Test
    void create_returns201() {
        SlideDtoIn in = SlideDtoIn.builder().title("t").build();
        Slide s = Slide.builder().build();
        when(mapper.toDomain(in)).thenReturn(s);
        when(useCase.create(s)).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(SlideDtoOut.builder().build());
        ResponseEntity<SlideDtoOut> r = controller.create(in, "tok");
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void update_returns200() {
        SlideDtoIn in = SlideDtoIn.builder().title("t").build();
        Slide s = Slide.builder().build();
        when(mapper.toDomain(in)).thenReturn(s);
        when(useCase.update("1", s)).thenReturn(s);
        when(mapper.toDto(s)).thenReturn(SlideDtoOut.builder().build());
        assertThat(controller.update("1", in, "tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_returns204() {
        assertThat(controller.delete("1", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("1");
    }

    @Test
    void updatePositions_returns204() {
        when(mapper.toDomain(any())).thenReturn(Slide.builder().build());
        assertThat(controller.updatePositions(List.of(SlideDtoIn.builder().title("t").build()), "tok").getStatusCode()
                .value()).isEqualTo(204);
    }

    private static <T> T any() {
        return org.mockito.ArgumentMatchers.any();
    }
}
