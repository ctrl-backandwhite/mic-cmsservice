package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.ContactMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactMessageJpaRepository
        extends JpaRepository<ContactMessageEntity, String>,
        JpaSpecificationExecutor<ContactMessageEntity> {
}
