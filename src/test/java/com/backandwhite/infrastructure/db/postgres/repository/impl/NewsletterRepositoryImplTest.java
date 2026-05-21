package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.NewsletterInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.NewsletterSubscriberJpaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class NewsletterRepositoryImplTest {

    @Mock
    private NewsletterSubscriberJpaRepository jpa;

    @Mock
    private NewsletterInfraMapper mapper;

    private NewsletterRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new NewsletterRepositoryImpl(jpa, mapper);
    }

    private NewsletterSubscriber sub() {
        return NewsletterSubscriber.builder().email("a@a.com").build();
    }

    @Test
    void save_assignsId() {
        NewsletterSubscriber in = sub();
        NewsletterSubscriberEntity entity = new NewsletterSubscriberEntity();
        NewsletterSubscriberEntity saved = new NewsletterSubscriberEntity();
        NewsletterSubscriber out = sub();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        NewsletterSubscriber r = repo.save(in);

        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void update_delegates() {
        NewsletterSubscriber in = sub();
        in.setId("existing");
        NewsletterSubscriberEntity entity = new NewsletterSubscriberEntity();
        NewsletterSubscriberEntity saved = new NewsletterSubscriberEntity();
        NewsletterSubscriber out = sub();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("existing");
    }

    @Test
    void findById_present() {
        NewsletterSubscriberEntity e = new NewsletterSubscriberEntity();
        when(jpa.findById("x")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(sub());
        assertThat(repo.findById("x")).isPresent();
    }

    @Test
    void findById_empty() {
        when(jpa.findById("x")).thenReturn(Optional.empty());
        assertThat(repo.findById("x")).isEmpty();
    }

    @Test
    void findByEmail_present() {
        NewsletterSubscriberEntity e = new NewsletterSubscriberEntity();
        when(jpa.findByEmail("a@a.com")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(sub());
        assertThat(repo.findByEmail("a@a.com")).isPresent();
    }

    @Test
    void findByEmail_empty() {
        when(jpa.findByEmail("x")).thenReturn(Optional.empty());
        assertThat(repo.findByEmail("x")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_emptyFilters() {
        NewsletterSubscriberEntity e = new NewsletterSubscriberEntity();
        Page<NewsletterSubscriberEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(sub());

        Page<NewsletterSubscriber> r = repo.findAll(Map.of(), PageRequest.of(0, 10));
        assertThat(r.getContent()).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_withFilters() {
        NewsletterSubscriberEntity e = new NewsletterSubscriberEntity();
        Page<NewsletterSubscriberEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(sub());

        Map<String, Object> filters = Map.of("status", "ACTIVE", "search", "Foo");
        assertThat(repo.findAll(filters, PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(jpa).deleteById("id");
    }

    @Test
    void count_delegates() {
        when(jpa.count()).thenReturn(7L);
        assertThat(repo.count()).isEqualTo(7L);
    }
}
