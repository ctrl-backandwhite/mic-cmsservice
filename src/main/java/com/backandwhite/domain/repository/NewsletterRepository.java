package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.NewsletterSubscriber;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsletterRepository {
    NewsletterSubscriber save(NewsletterSubscriber subscriber);

    NewsletterSubscriber update(NewsletterSubscriber subscriber);

    Optional<NewsletterSubscriber> findById(String id);

    Optional<NewsletterSubscriber> findByEmail(String email);

    Page<NewsletterSubscriber> findAll(Map<String, Object> filters, Pageable pageable);

    void delete(String id);

    long count();
}
