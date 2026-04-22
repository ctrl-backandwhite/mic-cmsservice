package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyRuleJpaRepository extends JpaRepository<LoyaltyRuleEntity, String> {

    /**
     * Returns the most recent active rule for the given action.
     *
     * <p>
     * The admin UI allows multiple rules to coexist (e.g. seasonal overrides), so
     * we never assume uniqueness — always pick the latest one to avoid
     * {@code NonUniqueResultException} crashing the loyalty pipeline.
     */
    Optional<LoyaltyRuleEntity> findFirstByActionAndActiveTrueOrderByCreatedAtDesc(LoyaltyAction action);
}
