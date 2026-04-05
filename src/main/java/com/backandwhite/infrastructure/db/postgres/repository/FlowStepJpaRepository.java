package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.FlowStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlowStepJpaRepository extends JpaRepository<FlowStepEntity, String> {

    List<FlowStepEntity> findByFlowIdOrderByPositionAsc(String flowId);

    void deleteByFlowId(String flowId);
}
