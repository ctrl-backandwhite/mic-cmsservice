package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.repository.FlowRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.FlowInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.FlowJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.FlowStepJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FlowRepositoryImpl implements FlowRepository {

    private final FlowJpaRepository flowJpa;
    private final FlowStepJpaRepository stepJpa;
    private final FlowInfraMapper mapper;

    @Override
    public Flow save(Flow flow) {
        flow.setId(UUID.randomUUID().toString());
        return mapper.toDomain(flowJpa.save(mapper.toEntity(flow)));
    }

    @Override
    public Flow update(Flow flow) {
        return mapper.toDomain(flowJpa.save(mapper.toEntity(flow)));
    }

    @Override
    public Optional<Flow> findById(String id) {
        return flowJpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Flow> findAll() {
        return flowJpa.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        flowJpa.deleteById(id);
    }

    // Steps

    @Override
    public FlowStep saveStep(FlowStep step) {
        step.setId(UUID.randomUUID().toString());
        return mapper.toStepDomain(stepJpa.save(mapper.toStepEntity(step)));
    }

    @Override
    public List<FlowStep> findStepsByFlowId(String flowId) {
        return stepJpa.findByFlowIdOrderByPositionAsc(flowId).stream()
                .map(mapper::toStepDomain)
                .toList();
    }

    @Override
    public void deleteStepsByFlowId(String flowId) {
        stepJpa.deleteByFlowId(flowId);
    }
}
