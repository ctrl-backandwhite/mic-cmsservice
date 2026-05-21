package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.infrastructure.db.postgres.entity.FlowEntity;
import com.backandwhite.infrastructure.db.postgres.entity.FlowStepEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.FlowInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.FlowJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.FlowStepJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlowRepositoryImplTest {

    @Mock
    private FlowJpaRepository flowJpa;

    @Mock
    private FlowStepJpaRepository stepJpa;

    @Mock
    private FlowInfraMapper mapper;

    private FlowRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new FlowRepositoryImpl(flowJpa, stepJpa, mapper);
    }

    @Test
    void save_assignsId() {
        Flow in = Flow.builder().name("f").build();
        FlowEntity entity = new FlowEntity();
        FlowEntity saved = new FlowEntity();
        Flow out = Flow.builder().id("x").name("f").build();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(flowJpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        Flow r = repo.save(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void update_keepsId() {
        Flow in = Flow.builder().id("kept").name("f").build();
        FlowEntity entity = new FlowEntity();
        FlowEntity saved = new FlowEntity();
        Flow out = Flow.builder().id("kept").name("f").build();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(flowJpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("kept");
    }

    @Test
    void findById_present() {
        FlowEntity e = new FlowEntity();
        when(flowJpa.findById("id")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(Flow.builder().id("id").build());
        assertThat(repo.findById("id")).isPresent();
    }

    @Test
    void findById_empty() {
        when(flowJpa.findById("id")).thenReturn(Optional.empty());
        assertThat(repo.findById("id")).isEmpty();
    }

    @Test
    void findAll_delegates() {
        FlowEntity e = new FlowEntity();
        when(flowJpa.findAll()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(Flow.builder().id("a").build());
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(flowJpa).deleteById("id");
    }

    @Test
    void saveStep_assignsId() {
        FlowStep in = FlowStep.builder().flowId("f1").position(1).build();
        FlowStepEntity entity = new FlowStepEntity();
        FlowStepEntity saved = new FlowStepEntity();
        FlowStep out = FlowStep.builder().id("x").flowId("f1").build();

        when(mapper.toStepEntity(in)).thenReturn(entity);
        when(stepJpa.save(entity)).thenReturn(saved);
        when(mapper.toStepDomain(saved)).thenReturn(out);

        FlowStep r = repo.saveStep(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void findStepsByFlowId_delegates() {
        FlowStepEntity e = new FlowStepEntity();
        when(stepJpa.findByFlowIdOrderByPositionAsc("f1")).thenReturn(List.of(e));
        when(mapper.toStepDomain(e)).thenReturn(FlowStep.builder().id("s1").build());
        assertThat(repo.findStepsByFlowId("f1")).hasSize(1);
    }

    @Test
    void deleteStepsByFlowId_delegates() {
        repo.deleteStepsByFlowId("f1");
        verify(stepJpa).deleteByFlowId("f1");
    }
}
