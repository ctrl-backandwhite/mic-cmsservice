package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.infrastructure.db.postgres.entity.ContactMessageEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.ContactInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.ContactMessageJpaRepository;
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
class ContactRepositoryImplTest {

    @Mock
    private ContactMessageJpaRepository jpa;

    @Mock
    private ContactInfraMapper mapper;

    private ContactRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new ContactRepositoryImpl(jpa, mapper);
    }

    private ContactMessage domain() {
        return ContactMessage.builder().name("a").email("a@a.com").subject("s").message("m").build();
    }

    @Test
    void save_assignsIdAndDelegates() {
        ContactMessage in = domain();
        ContactMessageEntity entity = new ContactMessageEntity();
        ContactMessageEntity saved = new ContactMessageEntity();
        ContactMessage out = domain();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        ContactMessage result = repo.save(in);

        assertThat(in.getId()).isNotBlank();
        assertThat(result).isSameAs(out);
        verify(jpa).save(entity);
    }

    @Test
    void findById_present() {
        ContactMessageEntity e = new ContactMessageEntity();
        ContactMessage d = domain();
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
    void findAll_paged() {
        ContactMessageEntity e = new ContactMessageEntity();
        Page<ContactMessageEntity> page = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(PageRequest.class))).thenReturn(page);
        when(mapper.toDomain(e)).thenReturn(domain());

        Page<ContactMessage> result = repo.findAll(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void update_delegates() {
        ContactMessage in = domain();
        in.setId("existing");
        ContactMessageEntity entity = new ContactMessageEntity();
        ContactMessageEntity saved = new ContactMessageEntity();
        ContactMessage out = domain();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("existing");
    }
}
