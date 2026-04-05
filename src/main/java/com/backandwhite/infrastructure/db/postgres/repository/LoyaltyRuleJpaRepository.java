package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyRuleJpaRepository extends JpaRepository<LoyaltyRuleEntity, String> {
}
