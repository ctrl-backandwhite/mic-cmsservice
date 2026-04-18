package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTierEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyTierJpaRepository extends JpaRepository<LoyaltyTierEntity, String> {

    List<LoyaltyTierEntity> findAllByOrderByMinPointsAsc();
}
