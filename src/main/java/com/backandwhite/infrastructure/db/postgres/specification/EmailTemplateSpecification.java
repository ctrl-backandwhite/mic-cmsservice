package com.backandwhite.infrastructure.db.postgres.specification;

import com.backandwhite.domain.valueobject.EmailTrigger;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class EmailTemplateSpecification {

    private static final String CATEGORY = "category";
    private static final String TRIGGER_TYPE = "triggerType";

    private EmailTemplateSpecification() {
    }

    public static Specification<EmailTemplateEntity> withFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filters.containsKey(CATEGORY)) {
                predicate = cb.and(predicate, cb.equal(root.get(CATEGORY), filters.get(CATEGORY).toString()));
            }
            if (filters.containsKey(TRIGGER_TYPE)) {
                predicate = cb.and(predicate,
                        cb.equal(root.get(TRIGGER_TYPE), EmailTrigger.valueOf(filters.get(TRIGGER_TYPE).toString())));
            }
            if (filters.containsKey("search")) {
                String search = "%" + filters.get("search").toString().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), search));
            }

            return predicate;
        };
    }
}
