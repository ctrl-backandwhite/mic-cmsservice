package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.repository.EmailTemplateRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.EmailTemplateInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.EmailTemplateJpaRepository;
import com.backandwhite.infrastructure.db.postgres.specification.EmailTemplateSpecification;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailTemplateRepositoryImpl implements EmailTemplateRepository {

    private final EmailTemplateJpaRepository jpa;
    private final EmailTemplateInfraMapper mapper;

    @Override
    public EmailTemplate save(EmailTemplate template) {
        template.setId(UUID.randomUUID().toString());
        return mapper.toDomain(jpa.save(mapper.toEntity(template)));
    }

    @Override
    public EmailTemplate update(EmailTemplate template) {
        return mapper.toDomain(jpa.save(mapper.toEntity(template)));
    }

    @Override
    public Optional<EmailTemplate> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<EmailTemplate> findByName(String name) {
        return jpa.findByName(name).map(mapper::toDomain);
    }

    @Override
    public Page<EmailTemplate> findAll(Map<String, Object> filters, Pageable pageable) {
        return jpa.findAll(EmailTemplateSpecification.withFilters(filters), pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }
}
