package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.Slide;
import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.SlideInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SlideJpaRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlideRepositoryImplTest {

    @Mock
    private SlideJpaRepository jpa;

    @Mock
    private SlideInfraMapper mapper;

    private SlideRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new SlideRepositoryImpl(jpa, mapper);
    }

    private Slide slide() {
        return Slide.builder().title("t").position(1).build();
    }

    @Test
    void save_assignsId() {
        Slide in = slide();
        SlideEntity entity = new SlideEntity();
        SlideEntity saved = new SlideEntity();
        Slide out = slide();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        Slide r = repo.save(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void update_keepsId() {
        Slide in = slide();
        in.setId("s1");
        SlideEntity entity = new SlideEntity();
        SlideEntity saved = new SlideEntity();
        Slide out = slide();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("s1");
    }

    @Test
    void findById_presentAndEmpty() {
        SlideEntity e = new SlideEntity();
        when(jpa.findById("id")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(slide());
        assertThat(repo.findById("id")).isPresent();

        when(jpa.findById("idx")).thenReturn(Optional.empty());
        assertThat(repo.findById("idx")).isEmpty();
    }

    @Test
    void findAllActive_noLocale() {
        SlideEntity e = new SlideEntity();
        when(jpa.findByActiveTrueOrderByPositionAsc()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(slide());
        assertThat(repo.findAllActive()).hasSize(1);
    }

    @Test
    void findAllActive_localeNull_fallsBack() {
        SlideEntity e = new SlideEntity();
        when(jpa.findByActiveTrueOrderByPositionAsc()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(slide());
        assertThat(repo.findAllActive((String) null)).hasSize(1);
    }

    @Test
    void findAllActive_localeBlank_fallsBack() {
        SlideEntity e = new SlideEntity();
        when(jpa.findByActiveTrueOrderByPositionAsc()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(slide());
        assertThat(repo.findAllActive("   ")).hasSize(1);
    }

    @Test
    void findAllActive_locale_translatesAndConvertsTimestamps() {
        Instant created = Instant.parse("2025-01-01T00:00:00Z");
        Object[] row = new Object[]{"id1", "title", "subtitle", "img", "link", "btn", Integer.valueOf(3), Boolean.TRUE,
                Timestamp.from(created), LocalDateTime.of(2025, 2, 1, 0, 0, 0)};
        java.util.List<Object[]> rows = new java.util.ArrayList<>();
        rows.add(row);
        when(jpa.findActiveTranslated("es")).thenReturn(rows);

        List<Slide> r = repo.findAllActive("es");
        assertThat(r).hasSize(1);
        assertThat(r.get(0).getId()).isEqualTo("id1");
        assertThat(r.get(0).getPosition()).isEqualTo(3);
        assertThat(r.get(0).isActive()).isTrue();
        assertThat(r.get(0).getCreatedAt()).isEqualTo(created);
        assertThat(r.get(0).getUpdatedAt())
                .isEqualTo(LocalDateTime.of(2025, 2, 1, 0, 0).atZone(ZoneOffset.UTC).toInstant());
    }

    @Test
    void findAllActive_locale_handlesAllInstantVariantsAndNulls() {
        Instant inst = Instant.parse("2025-03-01T00:00:00Z");
        OffsetDateTime odt = OffsetDateTime.parse("2025-04-01T00:00:00Z");
        ZonedDateTime zdt = ZonedDateTime.parse("2025-05-01T00:00:00Z");

        // Row using Instant directly + OffsetDateTime
        Object[] row1 = new Object[]{"id-inst", "t", "s", "i", "l", "b", null, null, inst, odt};
        // Row using ZonedDateTime + null
        Object[] row2 = new Object[]{"id-z", "t", "s", "i", "l", "b", null, null, zdt, null};
        // Row using unsupported type (returns null)
        Object[] row3 = new Object[]{"id-x", "t", "s", "i", "l", "b", null, null, "not a date", "neither"};
        when(jpa.findActiveTranslated("en")).thenReturn(List.of(row1, row2, row3));

        List<Slide> r = repo.findAllActive("en");
        assertThat(r).hasSize(3);
        assertThat(r.get(0).getCreatedAt()).isEqualTo(inst);
        assertThat(r.get(0).getUpdatedAt()).isEqualTo(odt.toInstant());
        assertThat(r.get(0).getPosition()).isZero();
        assertThat(r.get(0).isActive()).isFalse();
        assertThat(r.get(1).getCreatedAt()).isEqualTo(zdt.toInstant());
        assertThat(r.get(1).getUpdatedAt()).isNull();
        assertThat(r.get(2).getCreatedAt()).isNull();
        assertThat(r.get(2).getUpdatedAt()).isNull();
    }

    @Test
    void findAll_delegates() {
        SlideEntity e = new SlideEntity();
        when(jpa.findAllByOrderByPositionAsc()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(slide());
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(jpa).deleteById("id");
    }

    @Test
    void updatePositions_savesAll() {
        Slide s1 = slide();
        Slide s2 = slide();
        SlideEntity e1 = new SlideEntity();
        SlideEntity e2 = new SlideEntity();
        when(mapper.toEntity(s1)).thenReturn(e1);
        when(mapper.toEntity(s2)).thenReturn(e2);

        repo.updatePositions(List.of(s1, s2));

        verify(jpa).saveAll(anyList());
    }
}
