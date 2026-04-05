package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.Slide;
import com.backandwhite.domain.repository.SlideRepository;
import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.SlideInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SlideJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlideRepositoryImpl implements SlideRepository {

    private final SlideJpaRepository jpa;
    private final SlideInfraMapper mapper;

    @Override
    public Slide save(Slide slide) {
        slide.setId(UUID.randomUUID().toString());
        return mapper.toDomain(jpa.save(mapper.toEntity(slide)));
    }

    @Override
    public Slide update(Slide slide) {
        return mapper.toDomain(jpa.save(mapper.toEntity(slide)));
    }

    @Override
    public Optional<Slide> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Slide> findAllActive() {
        return jpa.findByActiveTrueOrderByPositionAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Slide> findAll() {
        return jpa.findAllByOrderByPositionAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }

    @Override
    public void updatePositions(List<Slide> slides) {
        List<SlideEntity> entities = slides.stream()
                .map(mapper::toEntity)
                .toList();
        jpa.saveAll(entities);
    }
}
