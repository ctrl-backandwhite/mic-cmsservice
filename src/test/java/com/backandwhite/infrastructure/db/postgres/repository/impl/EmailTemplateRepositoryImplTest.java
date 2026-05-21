package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.EmailTemplateInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.EmailTemplateJpaRepository;
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
class EmailTemplateRepositoryImplTest {

    @Mock
    private EmailTemplateJpaRepository jpa;

    @Mock
    private EmailTemplateInfraMapper mapper;

    private EmailTemplateRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new EmailTemplateRepositoryImpl(jpa, mapper);
    }

    private EmailTemplate tpl() {
        return EmailTemplate.builder().name("welcome").subject("hi").bodyHtml("<p>hi</p>").build();
    }

    @Test
    void save_assignsId() {
        EmailTemplate in = tpl();
        EmailTemplateEntity entity = new EmailTemplateEntity();
        EmailTemplateEntity saved = new EmailTemplateEntity();
        EmailTemplate out = tpl();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        EmailTemplate r = repo.save(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void update_delegates() {
        EmailTemplate in = tpl();
        in.setId("existing");
        EmailTemplateEntity entity = new EmailTemplateEntity();
        EmailTemplateEntity saved = new EmailTemplateEntity();
        EmailTemplate out = tpl();
        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("existing");
    }

    @Test
    void findById_presentAndEmpty() {
        EmailTemplateEntity e = new EmailTemplateEntity();
        when(jpa.findById("x")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(tpl());
        assertThat(repo.findById("x")).isPresent();

        when(jpa.findById("y")).thenReturn(Optional.empty());
        assertThat(repo.findById("y")).isEmpty();
    }

    @Test
    void findByName_present() {
        EmailTemplateEntity e = new EmailTemplateEntity();
        when(jpa.findByName("welcome")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(tpl());
        assertThat(repo.findByName("welcome")).isPresent();
    }

    @Test
    void findByName_empty() {
        when(jpa.findByName("x")).thenReturn(Optional.empty());
        assertThat(repo.findByName("x")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_emptyFilters() {
        EmailTemplateEntity e = new EmailTemplateEntity();
        Page<EmailTemplateEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(tpl());

        assertThat(repo.findAll(Map.of(), PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_withAllFilters() {
        EmailTemplateEntity e = new EmailTemplateEntity();
        Page<EmailTemplateEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(tpl());

        Map<String, Object> filters = Map.of("category", "transactional", "triggerType", "WELCOME", "search", "Hi");
        assertThat(repo.findAll(filters, PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(jpa).deleteById("id");
    }
}
