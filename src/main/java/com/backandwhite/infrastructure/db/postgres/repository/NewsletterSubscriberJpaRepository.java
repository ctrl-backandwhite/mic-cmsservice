package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NewsletterSubscriberJpaRepository
        extends JpaRepository<NewsletterSubscriberEntity, String>,
        JpaSpecificationExecutor<NewsletterSubscriberEntity> {

    Optional<NewsletterSubscriberEntity> findByEmail(String email);
}
