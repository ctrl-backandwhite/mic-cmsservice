package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyTierJpaRepository extends JpaRepository<LoyaltyTierEntity, String> {

    List<LoyaltyTierEntity> findAllByOrderByMinPointsAsc();
}
