package com.backandwhite.application.usecase;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.domain.model.NewsletterSubscriber;
import java.util.Map;

public interface NewsletterUseCase {
    NewsletterSubscriber subscribe(String email, String source);

    void unsubscribe(String email);

    NewsletterSubscriber findById(String id);

    PaginationDtoOut<NewsletterSubscriber> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending);

    void delete(String id);

    long count();
}
