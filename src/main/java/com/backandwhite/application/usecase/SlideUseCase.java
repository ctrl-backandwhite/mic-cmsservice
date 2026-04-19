package com.backandwhite.application.usecase;

import com.backandwhite.domain.model.Slide;
import java.util.List;

public interface SlideUseCase {
    Slide create(Slide slide);

    Slide update(String id, Slide slide);

    Slide findById(String id);

    List<Slide> findAll();

    List<Slide> findAllActive();

    /** Active slides with fields translated for the given locale. */
    List<Slide> findAllActive(String locale);

    void delete(String id);

    void updatePositions(List<Slide> slides);
}
