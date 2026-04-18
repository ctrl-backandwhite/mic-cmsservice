package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailTemplateJpaRepository
        extends
            JpaRepository<EmailTemplateEntity, String>,
            JpaSpecificationExecutor<EmailTemplateEntity> {

    Optional<EmailTemplateEntity> findByName(String name);
}
