package com.backandwhite.application.usecase.impl;

import com.backandwhite.application.usecase.FlowUseCase;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.repository.FlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FlowUseCaseImpl implements FlowUseCase {

    private final FlowRepository flowRepository;

    @Override
    @Transactional
    public Flow create(Flow flow) {
        return flowRepository.save(flow);
    }

    @Override
    @Transactional
    public Flow update(String id, Flow flow) {
        flowRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Flow", id));
        flow.setId(id);
        return flowRepository.update(flow);
    }

    @Override
    @Transactional(readOnly = true)
    public Flow findById(String id) {
        Flow flow = flowRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Flow", id));
        flow.setSteps(flowRepository.findStepsByFlowId(id));
        return flow;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flow> findAll() {
        return flowRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) {
        flowRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Flow", id));
        flowRepository.deleteStepsByFlowId(id);
        flowRepository.delete(id);
    }

    @Override
    @Transactional
    public List<FlowStep> syncSteps(String flowId, List<FlowStep> steps) {
        flowRepository.findById(flowId)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Flow", flowId));
        flowRepository.deleteStepsByFlowId(flowId);
        List<FlowStep> saved = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            FlowStep step = steps.get(i);
            step.setFlowId(flowId);
            step.setPosition(i + 1);
            saved.add(flowRepository.saveStep(step));
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlowStep> findStepsByFlowId(String flowId) {
        return flowRepository.findStepsByFlowId(flowId);
    }
}
