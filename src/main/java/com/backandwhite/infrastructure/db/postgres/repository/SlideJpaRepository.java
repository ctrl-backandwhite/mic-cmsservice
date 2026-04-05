package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideJpaRepository extends JpaRepository<SlideEntity, String> {

    List<SlideEntity> findByActiveTrueOrderByPositionAsc();

    List<SlideEntity> findAllByOrderByPositionAsc();
}
