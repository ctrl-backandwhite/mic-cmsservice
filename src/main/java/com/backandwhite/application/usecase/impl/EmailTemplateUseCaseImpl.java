package com.backandwhite.application.usecase.impl;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.application.usecase.EmailTemplateUseCase;
import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EmailTemplateUseCaseImpl implements EmailTemplateUseCase {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    @Transactional
    public EmailTemplate create(EmailTemplate template) {
        return emailTemplateRepository.save(template);
    }

    @Override
    @Transactional
    public EmailTemplate update(String id, EmailTemplate template) {
        emailTemplateRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("EmailTemplate", id));
        template.setId(id);
        return emailTemplateRepository.update(template);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplate findById(String id) {
        return emailTemplateRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("EmailTemplate", id));
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplate findByName(String name) {
        return emailTemplateRepository.findByName(name)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("EmailTemplate", name));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<EmailTemplate> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(emailTemplateRepository.findAll(filters, pageable));
    }

    @Override
    @Transactional
    public void delete(String id) {
        emailTemplateRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("EmailTemplate", id));
        emailTemplateRepository.delete(id);
    }
}
