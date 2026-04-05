package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowJpaRepository extends JpaRepository<FlowEntity, String> {
}
