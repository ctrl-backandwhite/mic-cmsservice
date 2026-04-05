package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.SeoPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SeoPageJpaRepository
        extends JpaRepository<SeoPageEntity, String>,
        JpaSpecificationExecutor<SeoPageEntity> {

    Optional<SeoPageEntity> findByPath(String path);
}
