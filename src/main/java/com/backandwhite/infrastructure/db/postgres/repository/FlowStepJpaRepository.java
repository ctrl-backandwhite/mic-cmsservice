package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.FlowStepEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowStepJpaRepository extends JpaRepository<FlowStepEntity, String> {

    List<FlowStepEntity> findByFlowIdOrderByPositionAsc(String flowId);

    void deleteByFlowId(String flowId);
}
