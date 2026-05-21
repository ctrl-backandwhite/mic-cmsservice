package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.infrastructure.db.postgres.entity.SeoPageEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.SeoPageInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SeoPageJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class SeoPageRepositoryImplTest {

    @Mock
    private SeoPageJpaRepository jpa;

    @Mock
    private SeoPageInfraMapper mapper;

    private SeoPageRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new SeoPageRepositoryImpl(jpa, mapper);
    }

    private SeoPage page() {
        return SeoPage.builder().path("/").metaTitle("t").build();
    }

    @Test
    void save_assignsIdAndDelegates() {
        SeoPage in = page();
        SeoPageEntity entity = new SeoPageEntity();
        SeoPageEntity saved = new SeoPageEntity();
        SeoPage out = page();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        SeoPage result = repo.save(in);

        assertThat(in.getId()).isNotBlank();
        assertThat(result).isSameAs(out);
    }

    @Test
    void update_keepsId() {
        SeoPage in = page();
        in.setId("existing");
        SeoPageEntity entity = new SeoPageEntity();
        SeoPageEntity saved = new SeoPageEntity();
        SeoPage out = page();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("existing");
    }

    @Test
    void findById_present() {
        SeoPageEntity e = new SeoPageEntity();
        SeoPage d = page();
        when(jpa.findById("id")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(d);

        assertThat(repo.findById("id")).contains(d);
    }

    @Test
    void findById_empty() {
        when(jpa.findById("nope")).thenReturn(Optional.empty());
        assertThat(repo.findById("nope")).isEmpty();
    }

    @Test
    void findByPath_present() {
        SeoPageEntity e = new SeoPageEntity();
        SeoPage d = page();
        when(jpa.findByPath("/home")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(d);

        assertThat(repo.findByPath("/home")).contains(d);
    }

    @Test
    void findByPath_empty() {
        when(jpa.findByPath("/x")).thenReturn(Optional.empty());
        assertThat(repo.findByPath("/x")).isEmpty();
    }

    @Test
    void findAll_paged() {
        SeoPageEntity e = new SeoPageEntity();
        Page<SeoPageEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(PageRequest.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(page());

        assertThat(repo.findAll(PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(jpa).deleteById("id");
    }
}
