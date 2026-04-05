package com.backandwhite.infrastructure.db.postgres.specification;

import com.backandwhite.domain.valueobject.NewsletterStatus;
import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class NewsletterSpecification {

    private NewsletterSpecification() {
    }

    public static Specification<NewsletterSubscriberEntity> withFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filters.containsKey("status")) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"),
                                NewsletterStatus.valueOf(filters.get("status").toString())));
            }
            if (filters.containsKey("search")) {
                String search = "%" + filters.get("search").toString().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("email")), search));
            }

            return predicate;
        };
    }
}
