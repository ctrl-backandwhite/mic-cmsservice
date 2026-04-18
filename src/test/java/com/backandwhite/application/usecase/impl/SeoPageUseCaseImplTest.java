package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.domain.repository.SeoPageRepository;
import java.util.List;
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
class SeoPageUseCaseImplTest {

    @Mock
    private SeoPageRepository repository;

    @InjectMocks
    private SeoPageUseCaseImpl useCase;

    private SeoPage page() {
        return SeoPage.builder().id("p1").path("/x").metaTitle("T").indexable(true).build();
    }

    @Test
    void create_delegates() {
        SeoPage p = page();
        when(repository.save(p)).thenReturn(p);
        assertThat(useCase.create(p)).isSameAs(p);
    }

    @Test
    void update_existing_setsId() {
        when(repository.findById("p1")).thenReturn(Optional.of(page()));
        when(repository.update(any())).thenAnswer(i -> i.getArgument(0));
        SeoPage upd = page().withId(null).withMetaTitle("new");
        SeoPage result = useCase.update("p1", upd);
        assertThat(result.getId()).isEqualTo("p1");
    }

    @Test
    void update_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.update("x", page())).isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).update(any());
    }

    @Test
    void findById_existing() {
        when(repository.findById("p1")).thenReturn(Optional.of(page()));
        assertThat(useCase.findById("p1")).isNotNull();
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findByPath_existing() {
        when(repository.findByPath("/a")).thenReturn(Optional.of(page()));
        assertThat(useCase.findByPath("/a")).isNotNull();
    }

    @Test
    void findByPath_notFound() {
        when(repository.findByPath("/x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findByPath("/x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_asc() {
        Page<SeoPage> pg = new PageImpl<>(List.of(page()));
        when(repository.findAll(any(Pageable.class))).thenReturn(pg);
        assertThat(useCase.findAll(0, 10, "path", true).content()).hasSize(1);
    }

    @Test
    void findAll_desc() {
        Page<SeoPage> pg = new PageImpl<>(List.of(page()));
        when(repository.findAll(any(Pageable.class))).thenReturn(pg);
        assertThat(useCase.findAll(0, 10, "path", false).content()).hasSize(1);
    }

    @Test
    void delete_existing() {
        when(repository.findById("p1")).thenReturn(Optional.of(page()));
        useCase.delete("p1");
        verify(repository).delete("p1");
    }

    @Test
    void delete_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
    }
}
