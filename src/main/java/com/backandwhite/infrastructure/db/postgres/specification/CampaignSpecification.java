package com.backandwhite.infrastructure.db.postgres.specification;

import com.backandwhite.domain.valueobject.CampaignType;
import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class CampaignSpecification {

    private static final String ACTIVE = "active";

    private CampaignSpecification() {
    }

    public static Specification<CampaignEntity> withFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filters.containsKey(ACTIVE)) {
                predicate = cb.and(predicate,
                        cb.equal(root.get(ACTIVE), Boolean.valueOf(filters.get(ACTIVE).toString())));
            }
            if (filters.containsKey("type")) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("type"), CampaignType.valueOf(filters.get("type").toString())));
            }
            if (filters.containsKey("search")) {
                String search = "%" + filters.get("search").toString().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), search));
            }

            return predicate;
        };
    }
}
