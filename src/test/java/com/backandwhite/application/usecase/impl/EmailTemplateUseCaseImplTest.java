package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.repository.EmailTemplateRepository;
import com.backandwhite.domain.valueobject.EmailTrigger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class EmailTemplateUseCaseImplTest {

    @Mock
    private EmailTemplateRepository repository;

    @InjectMocks
    private EmailTemplateUseCaseImpl useCase;

    private EmailTemplate template() {
        return EmailTemplate.builder().id("t1").name("welcome").triggerType(EmailTrigger.WELCOME).subject("hi")
                .bodyHtml("<p>hi</p>").active(true).build();
    }

    @Test
    void create_saves() {
        EmailTemplate t = template();
        when(repository.save(t)).thenReturn(t);
        assertThat(useCase.create(t)).isSameAs(t);
    }

    @Test
    void update_existing_setsId() {
        EmailTemplate existing = template();
        when(repository.findById("t1")).thenReturn(Optional.of(existing));
        EmailTemplate update = template().withId(null).withName("new");
        when(repository.update(any(EmailTemplate.class))).thenAnswer(i -> i.getArgument(0));

        EmailTemplate result = useCase.update("t1", update);
        assertThat(result.getId()).isEqualTo("t1");
        assertThat(result.getName()).isEqualTo("new");
    }

    @Test
    void update_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.update("x", template())).isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).update(any());
    }

    @Test
    void findById_existing() {
        EmailTemplate t = template();
        when(repository.findById("t1")).thenReturn(Optional.of(t));
        assertThat(useCase.findById("t1")).isSameAs(t);
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findByName_existing() {
        EmailTemplate t = template();
        when(repository.findByName("welcome")).thenReturn(Optional.of(t));
        assertThat(useCase.findByName("welcome")).isSameAs(t);
    }

    @Test
    void findByName_notFound() {
        when(repository.findByName("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findByName("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_asc() {
        Page<EmailTemplate> page = new PageImpl<>(List.of(template()));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(page);

        assertThat(useCase.findAll(Map.of(), 0, 10, "name", true).content()).hasSize(1);
    }

    @Test
    void findAll_desc() {
        Page<EmailTemplate> page = new PageImpl<>(List.of(template()));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(page);

        assertThat(useCase.findAll(Map.of(), 0, 10, "name", false).content()).hasSize(1);
    }

    @Test
    void delete_existing() {
        when(repository.findById("t1")).thenReturn(Optional.of(template()));
        useCase.delete("t1");
        verify(repository).delete("t1");
    }

    @Test
    void delete_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
    }
}
