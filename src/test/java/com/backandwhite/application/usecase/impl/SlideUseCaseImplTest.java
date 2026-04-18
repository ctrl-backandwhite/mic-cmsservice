package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.Slide;
import com.backandwhite.domain.repository.SlideRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlideUseCaseImplTest {

    @Mock
    private SlideRepository slideRepository;

    @InjectMocks
    private SlideUseCaseImpl useCase;

    private Slide slide() {
        return Slide.builder().id("s1").title("t").imageUrl("u").position(1).active(true).build();
    }

    @Test
    void create_delegatesToRepository() {
        Slide s = slide();
        when(slideRepository.save(s)).thenReturn(s);

        assertThat(useCase.create(s)).isSameAs(s);
        verify(slideRepository).save(s);
    }

    @Test
    void update_existing_setsIdAndUpdates() {
        Slide existing = slide();
        Slide update = Slide.builder().title("new").build();
        when(slideRepository.findById("s1")).thenReturn(Optional.of(existing));
        when(slideRepository.update(any(Slide.class))).thenAnswer(i -> i.getArgument(0));

        Slide result = useCase.update("s1", update);

        assertThat(result.getId()).isEqualTo("s1");
        verify(slideRepository).update(update);
    }

    @Test
    void update_notFound_throws() {
        when(slideRepository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.update("x", slide())).isInstanceOf(EntityNotFoundException.class);
        verify(slideRepository, never()).update(any());
    }

    @Test
    void findById_existing_returnsSlide() {
        Slide s = slide();
        when(slideRepository.findById("s1")).thenReturn(Optional.of(s));
        assertThat(useCase.findById("s1")).isSameAs(s);
    }

    @Test
    void findById_notFound_throws() {
        when(slideRepository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findById("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAll_delegates() {
        List<Slide> list = List.of(slide());
        when(slideRepository.findAll()).thenReturn(list);
        assertThat(useCase.findAll()).isSameAs(list);
    }

    @Test
    void findAllActive_delegates() {
        List<Slide> list = List.of(slide());
        when(slideRepository.findAllActive()).thenReturn(list);
        assertThat(useCase.findAllActive()).isSameAs(list);
    }

    @Test
    void delete_existing_deletesById() {
        when(slideRepository.findById("s1")).thenReturn(Optional.of(slide()));
        useCase.delete("s1");
        verify(slideRepository).delete("s1");
    }

    @Test
    void delete_notFound_throws() {
        when(slideRepository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
        verify(slideRepository, never()).delete(any());
    }

    @Test
    void updatePositions_delegates() {
        List<Slide> slides = List.of(slide());
        useCase.updatePositions(slides);
        verify(slideRepository).updatePositions(slides);
    }
}
