package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.common.exception.BusinessException;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.domain.repository.NewsletterRepository;
import com.backandwhite.domain.valueobject.NewsletterStatus;
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
class NewsletterUseCaseImplTest {

    @Mock
    private NewsletterRepository repository;

    @Mock
    private CmsEventPort eventPort;

    @InjectMocks
    private NewsletterUseCaseImpl useCase;

    private NewsletterSubscriber sub(NewsletterStatus status) {
        return NewsletterSubscriber.builder().id("s1").email("a@a.com").status(status).build();
    }

    @Test
    void subscribe_new_savesAndPublishes() {
        when(repository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(repository.save(any(NewsletterSubscriber.class))).thenAnswer(i -> i.getArgument(0));

        NewsletterSubscriber saved = useCase.subscribe("a@a.com", "footer");
        assertThat(saved.getStatus()).isEqualTo(NewsletterStatus.ACTIVE);
        verify(eventPort).publishNewsletterSubscribed("a@a.com", null, "footer");
    }

    @Test
    void subscribe_alreadyActive_throws() {
        when(repository.findByEmail("a@a.com")).thenReturn(Optional.of(sub(NewsletterStatus.ACTIVE)));
        assertThatThrownBy(() -> useCase.subscribe("a@a.com", null)).isInstanceOf(BusinessException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void subscribe_unsubscribed_reactivates() {
        NewsletterSubscriber existing = sub(NewsletterStatus.UNSUBSCRIBED);
        when(repository.findByEmail("a@a.com")).thenReturn(Optional.of(existing));
        when(repository.update(existing)).thenReturn(existing);

        NewsletterSubscriber result = useCase.subscribe("a@a.com", "src");
        assertThat(result.getStatus()).isEqualTo(NewsletterStatus.ACTIVE);
        assertThat(result.getUnsubscribedAt()).isNull();
        verify(repository).update(existing);
    }

    @Test
    void unsubscribe_existing_updatesAndPublishes() {
        NewsletterSubscriber s = sub(NewsletterStatus.ACTIVE);
        when(repository.findByEmail("a@a.com")).thenReturn(Optional.of(s));

        useCase.unsubscribe("a@a.com");
        assertThat(s.getStatus()).isEqualTo(NewsletterStatus.UNSUBSCRIBED);
        assertThat(s.getUnsubscribedAt()).isNotNull();
        verify(repository).update(s);
        verify(eventPort).publishNewsletterUnsubscribed("a@a.com", null);
    }

    @Test
    void unsubscribe_notFound() {
        when(repository.findByEmail("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.unsubscribe("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findById_existing() {
        when(repository.findById("s1")).thenReturn(Optional.of(sub(NewsletterStatus.ACTIVE)));
        assertThat(useCase.findById("s1")).isNotNull();
    }

    @Test
    void findById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_asc() {
        Page<NewsletterSubscriber> page = new PageImpl<>(List.of(sub(NewsletterStatus.ACTIVE)));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(page);
        assertThat(useCase.findAll(Map.of(), 0, 10, "email", true).content()).hasSize(1);
    }

    @Test
    void findAll_desc() {
        Page<NewsletterSubscriber> page = new PageImpl<>(List.of(sub(NewsletterStatus.ACTIVE)));
        when(repository.findAll(any(Map.class), any(Pageable.class))).thenReturn(page);
        assertThat(useCase.findAll(Map.of(), 0, 10, "email", false).content()).hasSize(1);
    }

    @Test
    void delete_existing() {
        when(repository.findById("s1")).thenReturn(Optional.of(sub(NewsletterStatus.ACTIVE)));
        useCase.delete("s1");
        verify(repository).delete("s1");
    }

    @Test
    void delete_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void count_delegates() {
        when(repository.count()).thenReturn(42L);
        assertThat(useCase.count()).isEqualTo(42L);
        verify(repository).count();
    }
}
