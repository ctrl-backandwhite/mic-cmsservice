package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.domain.repository.SeoPageRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.SeoPageInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SeoPageJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeoPageRepositoryImpl implements SeoPageRepository {

    private final SeoPageJpaRepository jpa;
    private final SeoPageInfraMapper mapper;

    @Override
    public SeoPage save(SeoPage seoPage) {
        seoPage.setId(UUID.randomUUID().toString());
        return mapper.toDomain(jpa.save(mapper.toEntity(seoPage)));
    }

    @Override
    public SeoPage update(SeoPage seoPage) {
        return mapper.toDomain(jpa.save(mapper.toEntity(seoPage)));
    }

    @Override
    public Optional<SeoPage> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SeoPage> findByPath(String path) {
        return jpa.findByPath(path).map(mapper::toDomain);
    }

    @Override
    public Page<SeoPage> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }
}
