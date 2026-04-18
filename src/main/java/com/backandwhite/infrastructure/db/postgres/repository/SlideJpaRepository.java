package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlideJpaRepository extends JpaRepository<SlideEntity, String> {

    List<SlideEntity> findByActiveTrueOrderByPositionAsc();

    List<SlideEntity> findAllByOrderByPositionAsc();
}
