package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoyaltyRuleJpaRepository extends JpaRepository<LoyaltyRuleEntity, String> {

    Optional<LoyaltyRuleEntity> findByActionAndActiveTrue(LoyaltyAction action);
}
