package com.backandwhite.application.usecase.impl;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.NewsletterUseCase;
import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.domain.repository.NewsletterRepository;
import com.backandwhite.domain.valueobject.NewsletterStatus;
import com.backandwhite.infrastructure.message.kafka.producer.CmsEventProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;
import static com.backandwhite.domain.exception.Message.NEWSLETTER_ALREADY_SUBSCRIBED;

@Service
@RequiredArgsConstructor
public class NewsletterUseCaseImpl implements NewsletterUseCase {

    private final NewsletterRepository newsletterRepository;
    private final Optional<CmsEventProducerService> cmsEventProducer;

    @Override
    @Transactional
    public NewsletterSubscriber subscribe(String email, String source) {
        var existing = newsletterRepository.findByEmail(email);
        if (existing.isPresent()) {
            var subscriber = existing.get();
            if (subscriber.getStatus() == NewsletterStatus.ACTIVE) {
                throw NEWSLETTER_ALREADY_SUBSCRIBED.toBusinessException();
            }
            // Re-subscribe
            subscriber.setStatus(NewsletterStatus.ACTIVE);
            subscriber.setSubscribedAt(Instant.now());
            subscriber.setUnsubscribedAt(null);
            return newsletterRepository.update(subscriber);
        }
        var subscriber = NewsletterSubscriber.builder()
                .email(email)
                .status(NewsletterStatus.ACTIVE)
                .subscribedAt(Instant.now())
                .source(source)
                .build();
        NewsletterSubscriber saved = newsletterRepository.save(subscriber);
        cmsEventProducer.ifPresent(p -> p.publishNewsletterSubscribed(email, null, source));
        return saved;
    }

    @Override
    @Transactional
    public void unsubscribe(String email) {
        var subscriber = newsletterRepository.findByEmail(email)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("NewsletterSubscriber", email));
        subscriber.setStatus(NewsletterStatus.UNSUBSCRIBED);
        subscriber.setUnsubscribedAt(Instant.now());
        newsletterRepository.update(subscriber);
        cmsEventProducer.ifPresent(p -> p.publishNewsletterUnsubscribed(email, null));
    }

    @Override
    @Transactional(readOnly = true)
    public NewsletterSubscriber findById(String id) {
        return newsletterRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("NewsletterSubscriber", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationDtoOut<NewsletterSubscriber> findAll(Map<String, Object> filters, int page, int size,
            String sortBy, boolean ascending) {
        var pageable = PageableUtils.toPageable(page, size, sortBy, ascending);
        return PageableUtils.toResponse(newsletterRepository.findAll(filters, pageable));
    }

    @Override
    @Transactional
    public void delete(String id) {
        newsletterRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("NewsletterSubscriber", id));
        newsletterRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return newsletterRepository.count();
    }
}
