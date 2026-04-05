package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.SeoPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeoPageRepository {
    SeoPage save(SeoPage seoPage);

    SeoPage update(SeoPage seoPage);

    Optional<SeoPage> findById(String id);

    Optional<SeoPage> findByPath(String path);

    Page<SeoPage> findAll(Pageable pageable);

    void delete(String id);
}
