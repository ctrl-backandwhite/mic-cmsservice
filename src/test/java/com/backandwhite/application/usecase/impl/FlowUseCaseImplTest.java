package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.repository.FlowRepository;
import com.backandwhite.domain.valueobject.FlowType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlowUseCaseImplTest {

    @Mock
    private FlowRepository repository;

    @InjectMocks
    private FlowUseCaseImpl useCase;

    private Flow flow() {
        return Flow.builder().id("f1").name("n").type(FlowType.DELIVERY).active(true).build();
    }

    private FlowStep step() {
        return FlowStep.builder().title("t").build();
    }

    @Test
    void create_delegates() {
        Flow f = flow();
        when(repository.save(f)).thenReturn(f);
        assertThat(useCase.create(f)).isSameAs(f);
    }

    @Test
    void update_existing_setsId() {
        when(repository.findById("f1")).thenReturn(Optional.of(flow()));
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));

        Flow result = useCase.update("f1", flow().withId(null).withName("new"));
        assertThat(result.getId()).isEqualTo("f1");
    }

    @Test
    void update_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.update("x", flow())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findById_existing_populatesSteps() {
        Flow f = flow();
        when(repository.findById("f1")).thenReturn(Optional.of(f));
        when(repository.findStepsByFlowId("f1")).thenReturn(List.of(step()));

        Flow result = useCase.findById("f1");
        assertThat(result.getSteps()).hasSize(1);
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_delegates() {
        List<Flow> list = List.of(flow());
        when(repository.findAll()).thenReturn(list);
        assertThat(useCase.findAll()).isSameAs(list);
    }

    @Test
    void delete_existing_deletesStepsThenFlow() {
        when(repository.findById("f1")).thenReturn(Optional.of(flow()));
        useCase.delete("f1");
        verify(repository).deleteStepsByFlowId("f1");
        verify(repository).delete("f1");
    }

    @Test
    void delete_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).delete(any());
    }

    @Test
    void syncSteps_existing_deletesAndReCreates() {
        when(repository.findById("f1")).thenReturn(Optional.of(flow()));
        FlowStep s1 = step().withTitle("a");
        FlowStep s2 = step().withTitle("b");
        when(repository.saveStep(any())).thenAnswer(i -> i.getArgument(0));

        List<FlowStep> saved = useCase.syncSteps("f1", List.of(s1, s2));
        assertThat(saved).hasSize(2);
        assertThat(saved.get(0).getPosition()).isEqualTo(1);
        assertThat(saved.get(1).getPosition()).isEqualTo(2);
        assertThat(saved.get(0).getFlowId()).isEqualTo("f1");
        verify(repository).deleteStepsByFlowId("f1");
    }

    @Test
    void syncSteps_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.syncSteps("x", List.of(step()))).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findStepsByFlowId_delegates() {
        List<FlowStep> list = List.of(step());
        when(repository.findStepsByFlowId("f1")).thenReturn(list);
        assertThat(useCase.findStepsByFlowId("f1")).isSameAs(list);
    }
}
