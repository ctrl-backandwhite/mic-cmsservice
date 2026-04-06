package com.backandwhite.application.usecase;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.EmailTemplate;
import java.util.Map;

public interface EmailTemplateUseCase {
    EmailTemplate create(EmailTemplate template);

    EmailTemplate update(String id, EmailTemplate template);

    EmailTemplate findById(String id);

    EmailTemplate findByName(String name);

    PageResult<EmailTemplate> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending);

    void delete(String id);
}
