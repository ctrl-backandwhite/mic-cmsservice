package com.backandwhite.application.usecase.impl;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.application.usecase.SeoPageUseCase;
import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.domain.repository.SeoPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SeoPageUseCaseImpl implements SeoPageUseCase {

    private final SeoPageRepository seoPageRepository;

    @Override
    @Transactional
    public SeoPage create(SeoPage seoPage) {
        return seoPageRepository.save(seoPage);
    }

    @Override
    @Transactional
    public SeoPage update(String id, SeoPage seoPage) {
        seoPageRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("SeoPage", id));
        seoPage.setId(id);
        return seoPageRepository.update(seoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public SeoPage findById(String id) {
        return seoPageRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("SeoPage", id));
    }

    @Override
    @Transactional(readOnly = true)
    public SeoPage findByPath(String path) {
        return seoPageRepository.findByPath(path)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("SeoPage", path));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<SeoPage> findAll(int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(seoPageRepository.findAll(pageable));
    }

    @Override
    @Transactional
    public void delete(String id) {
        seoPageRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("SeoPage", id));
        seoPageRepository.delete(id);
    }
}
