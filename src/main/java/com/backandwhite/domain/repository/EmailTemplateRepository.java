package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface EmailTemplateRepository {
    EmailTemplate save(EmailTemplate template);

    EmailTemplate update(EmailTemplate template);

    Optional<EmailTemplate> findById(String id);

    Optional<EmailTemplate> findByName(String name);

    Page<EmailTemplate> findAll(Map<String, Object> filters, Pageable pageable);

    void delete(String id);
}
