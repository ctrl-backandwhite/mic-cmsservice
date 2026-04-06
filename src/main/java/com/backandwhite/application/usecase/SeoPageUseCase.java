package com.backandwhite.application.usecase;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.SeoPage;

public interface SeoPageUseCase {
    SeoPage create(SeoPage seoPage);

    SeoPage update(String id, SeoPage seoPage);

    SeoPage findById(String id);

    SeoPage findByPath(String path);

    PageResult<SeoPage> findAll(int page, int size, String sortBy, boolean ascending);

    void delete(String id);
}
