package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.FlowDtoIn;
import com.backandwhite.api.dto.in.FlowStepDtoIn;
import com.backandwhite.api.dto.out.FlowDtoOut;
import com.backandwhite.api.dto.out.FlowStepDtoOut;
import com.backandwhite.api.mapper.FlowApiMapper;
import com.backandwhite.application.usecase.FlowUseCase;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.valueobject.FlowType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlowControllerTest {
    @Mock
    FlowUseCase useCase;
    @Mock
    FlowApiMapper mapper;
    @InjectMocks
    FlowController controller;

    @Test
    void findAll_ok() {
        when(useCase.findAll()).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAll("tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findById_ok() {
        Flow f = Flow.builder().id("f1").build();
        when(useCase.findById("f1")).thenReturn(f);
        when(mapper.toDto(f)).thenReturn(FlowDtoOut.builder().id("f1").build());
        assertThat(controller.findById("f1", "tok").getBody().getId()).isEqualTo("f1");
    }

    @Test
    void create_ok() {
        FlowDtoIn in = FlowDtoIn.builder().name("n").type(FlowType.DELIVERY).build();
        Flow f = Flow.builder().build();
        when(mapper.toDomain(any())).thenReturn(f);
        when(useCase.create(f)).thenReturn(f);
        when(mapper.toDto(f)).thenReturn(FlowDtoOut.builder().build());
        assertThat(controller.create(in, "tok").getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void update_ok() {
        FlowDtoIn in = FlowDtoIn.builder().name("n").type(FlowType.DELIVERY).build();
        Flow f = Flow.builder().build();
        when(mapper.toDomain(any())).thenReturn(f);
        when(useCase.update(eq("f1"), any())).thenReturn(f);
        when(mapper.toDto(f)).thenReturn(FlowDtoOut.builder().build());
        assertThat(controller.update("f1", in, "tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_ok() {
        assertThat(controller.delete("f1", "tok").getStatusCode().value()).isEqualTo(204);
        verify(useCase).delete("f1");
    }

    @Test
    void syncSteps_ok() {
        List<FlowStepDtoIn> dtos = List.of(FlowStepDtoIn.builder().title("t").build());
        when(mapper.toStepDomainList(dtos)).thenReturn(List.of(FlowStep.builder().build()));
        when(useCase.syncSteps(eq("f1"), any())).thenReturn(List.of(FlowStep.builder().build()));
        when(mapper.toStepDtoList(any())).thenReturn(List.of(FlowStepDtoOut.builder().build()));
        assertThat(controller.syncSteps("f1", dtos, "tok").getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findSteps_ok() {
        when(useCase.findStepsByFlowId("f1")).thenReturn(List.of());
        when(mapper.toStepDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findSteps("f1", "tok").getStatusCode().value()).isEqualTo(200);
    }
}
