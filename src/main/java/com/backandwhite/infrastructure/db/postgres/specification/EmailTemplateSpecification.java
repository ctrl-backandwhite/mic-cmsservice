package com.backandwhite.infrastructure.db.postgres.specification;

import com.backandwhite.domain.valueobject.EmailTrigger;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class EmailTemplateSpecification {

    private EmailTemplateSpecification() {
    }

    public static Specification<EmailTemplateEntity> withFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filters.containsKey("category")) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("category"), filters.get("category").toString()));
            }
            if (filters.containsKey("triggerType")) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("triggerType"),
                                EmailTrigger.valueOf(filters.get("triggerType").toString())));
            }
            if (filters.containsKey("search")) {
                String search = "%" + filters.get("search").toString().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), search));
            }

            return predicate;
        };
    }
}
