package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.Slide;
import com.backandwhite.domain.repository.SlideRepository;
import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.SlideInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SlideJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        return jpa.findByActiveTrueOrderByPositionAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Slide> findAllActive(String locale) {
        if (locale == null || locale.isBlank()) {
            return findAllActive();
        }
        return jpa.findActiveTranslated(locale).stream().map(this::rowToSlide).toList();
    }

    private Slide rowToSlide(Object[] r) {
        return Slide.builder().id((String) r[0]).title((String) r[1]).subtitle((String) r[2]).imageUrl((String) r[3])
                .link((String) r[4]).buttonText((String) r[5]).position(r[6] != null ? ((Number) r[6]).intValue() : 0)
                .active(r[7] != null && (Boolean) r[7]).createdAt(toInstant(r[8])).updatedAt(toInstant(r[9])).build();
    }

    private static java.time.Instant toInstant(Object value) {
        if (value == null)
            return null;
        if (value instanceof java.time.Instant i)
            return i;
        if (value instanceof java.sql.Timestamp ts)
            return ts.toInstant();
        if (value instanceof java.time.LocalDateTime ldt)
            return ldt.atZone(java.time.ZoneOffset.UTC).toInstant();
        if (value instanceof java.time.OffsetDateTime odt)
            return odt.toInstant();
        if (value instanceof java.time.ZonedDateTime zdt)
            return zdt.toInstant();
        return null;
    }

    @Override
    public List<Slide> findAll() {
        return jpa.findAllByOrderByPositionAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }

    @Override
    public void updatePositions(List<Slide> slides) {
        List<SlideEntity> entities = slides.stream().map(mapper::toEntity).toList();
        jpa.saveAll(entities);
    }
}
