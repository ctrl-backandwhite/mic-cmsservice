package com.backandwhite.application.usecase.impl;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

import com.backandwhite.application.usecase.SlideUseCase;
import com.backandwhite.domain.model.Slide;
import com.backandwhite.domain.repository.SlideRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlideUseCaseImpl implements SlideUseCase {

    private final SlideRepository slideRepository;

    @Override
    @Transactional
    public Slide create(Slide slide) {
        return slideRepository.save(slide);
    }

    @Override
    @Transactional
    public Slide update(String id, Slide slide) {
        slideRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Slide", id));
        slide.setId(id);
        return slideRepository.update(slide);
    }

    @Override
    @Transactional(readOnly = true)
    public Slide findById(String id) {
        return slideRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Slide", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Slide> findAll() {
        return slideRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Slide> findAllActive() {
        return slideRepository.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Slide> findAllActive(String locale) {
        return slideRepository.findAllActive(locale);
    }

    @Override
    @Transactional
    public void delete(String id) {
        slideRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Slide", id));
        slideRepository.delete(id);
    }

    @Override
    @Transactional
    public void updatePositions(List<Slide> slides) {
        slideRepository.updatePositions(slides);
    }
}
