package com.backandwhite.application.usecase;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.domain.model.SeoPage;

public interface SeoPageUseCase {
    SeoPage create(SeoPage seoPage);

    SeoPage update(String id, SeoPage seoPage);

    SeoPage findById(String id);

    SeoPage findByPath(String path);

    PaginationDtoOut<SeoPage> findAll(int page, int size, String sortBy, boolean ascending);

    void delete(String id);
}
