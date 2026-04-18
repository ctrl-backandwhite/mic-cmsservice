package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.domain.repository.ContactRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ContactUseCaseImplTest {

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactUseCaseImpl useCase;

    private ContactMessage msg() {
        return ContactMessage.builder().id("m1").name("n").email("e@e.com").subject("s").message("m").build();
    }

    @Test
    void submit_marksUnread_andSaves() {
        ContactMessage m = msg();
        m.setRead(true);
        when(repository.save(any(ContactMessage.class))).thenAnswer(i -> i.getArgument(0));

        ContactMessage saved = useCase.submit(m);

        assertThat(saved.isRead()).isFalse();
    }

    @Test
    void findById_existing() {
        ContactMessage m = msg();
        when(repository.findById("m1")).thenReturn(Optional.of(m));
        assertThat(useCase.findById("m1")).isSameAs(m);
    }

    @Test
    void findById_notFound_throws() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_ascending_usesAscendingSort() {
        Page<ContactMessage> page = new PageImpl<>(List.of(msg()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageResult<ContactMessage> result = useCase.findAll(0, 10, "createdAt", true);

        assertThat(result.content()).hasSize(1);
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(cap.capture());
        assertThat(cap.getValue().getSort().getOrderFor("createdAt").isAscending()).isTrue();
    }

    @Test
    void findAll_descending_usesDescendingSort() {
        Page<ContactMessage> page = new PageImpl<>(List.of(msg()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        useCase.findAll(0, 10, "createdAt", false);

        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(cap.capture());
        assertThat(cap.getValue().getSort().getOrderFor("createdAt").isDescending()).isTrue();
    }

    @Test
    void markAsRead_existing_updates() {
        ContactMessage m = msg();
        when(repository.findById("m1")).thenReturn(Optional.of(m));

        useCase.markAsRead("m1");

        assertThat(m.isRead()).isTrue();
        verify(repository).update(m);
    }

    @Test
    void markAsRead_notFound_throws() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.markAsRead("x")).isInstanceOf(EntityNotFoundException.class);
    }
}
