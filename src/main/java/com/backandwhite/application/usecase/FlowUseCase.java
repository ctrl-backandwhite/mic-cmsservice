package com.backandwhite.application.usecase;

import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import java.util.List;

public interface FlowUseCase {
    Flow create(Flow flow);

    Flow update(String id, Flow flow);

    Flow findById(String id);

    List<Flow> findAll();

    void delete(String id);

    // Steps sync
    List<FlowStep> syncSteps(String flowId, List<FlowStep> steps);

    List<FlowStep> findStepsByFlowId(String flowId);
}
