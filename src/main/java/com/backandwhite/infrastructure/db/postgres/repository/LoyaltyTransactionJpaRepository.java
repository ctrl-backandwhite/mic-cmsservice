package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoyaltyTransactionJpaRepository extends JpaRepository<LoyaltyTransactionEntity, String> {

    Page<LoyaltyTransactionEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.points), 0) FROM LoyaltyTransactionEntity t WHERE t.userId = :userId")
    int sumPointsByUserId(@Param("userId") String userId);
}
