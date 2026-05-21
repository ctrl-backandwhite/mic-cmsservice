package com.backandwhite.infrastructure.db.postgres.specification;

import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class GiftCardSpecification {

    private static final String STATUS = "status";

    private GiftCardSpecification() {
    }

    public static Specification<GiftCardEntity> withFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filters.containsKey(STATUS)) {
                predicate = cb.and(predicate,
                        cb.equal(root.get(STATUS), GiftCardStatus.valueOf(filters.get(STATUS).toString())));
            }
            if (filters.containsKey("search")) {
                String search = "%" + filters.get("search").toString().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("code")), search));
            }

            return predicate;
        };
    }
}
