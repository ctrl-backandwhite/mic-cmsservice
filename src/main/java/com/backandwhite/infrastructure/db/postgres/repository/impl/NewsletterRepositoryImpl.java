package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.domain.repository.NewsletterRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.NewsletterInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.NewsletterSubscriberJpaRepository;
import com.backandwhite.infrastructure.db.postgres.specification.NewsletterSpecification;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsletterRepositoryImpl implements NewsletterRepository {

    private final NewsletterSubscriberJpaRepository jpa;
    private final NewsletterInfraMapper mapper;

    @Override
    public NewsletterSubscriber save(NewsletterSubscriber subscriber) {
        subscriber.setId(UUID.randomUUID().toString());
        return mapper.toDomain(jpa.save(mapper.toEntity(subscriber)));
    }

    @Override
    public NewsletterSubscriber update(NewsletterSubscriber subscriber) {
        return mapper.toDomain(jpa.save(mapper.toEntity(subscriber)));
    }

    @Override
    public Optional<NewsletterSubscriber> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<NewsletterSubscriber> findByEmail(String email) {
        return jpa.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Page<NewsletterSubscriber> findAll(Map<String, Object> filters, Pageable pageable) {
        return jpa.findAll(NewsletterSpecification.withFilters(filters), pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }

    @Override
    public long count() {
        return jpa.count();
    }
}
