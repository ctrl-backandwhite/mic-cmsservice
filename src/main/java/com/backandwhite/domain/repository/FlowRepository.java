package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import java.util.List;
import java.util.Optional;

public interface FlowRepository {
    Flow save(Flow flow);

    Flow update(Flow flow);

    Optional<Flow> findById(String id);

    List<Flow> findAll();

    void delete(String id);

    // Steps
    FlowStep saveStep(FlowStep step);

    List<FlowStep> findStepsByFlowId(String flowId);

    void deleteStepsByFlowId(String flowId);
}
