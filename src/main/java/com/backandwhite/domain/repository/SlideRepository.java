package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.Slide;
import java.util.List;
import java.util.Optional;

public interface SlideRepository {
    Slide save(Slide slide);

    Slide update(Slide slide);

    Optional<Slide> findById(String id);

    List<Slide> findAllActive();

    List<Slide> findAll();

    void delete(String id);

    void updatePositions(List<Slide> slides);
}
