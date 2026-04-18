package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignJpaRepository
        extends
            JpaRepository<CampaignEntity, String>,
            JpaSpecificationExecutor<CampaignEntity> {

    List<CampaignEntity> findByActiveTrueAndStartDateBeforeAndEndDateAfter(Instant now1, Instant now2);

    @Query("""
            SELECT c FROM CampaignEntity c
            WHERE c.active = true
              AND c.startDate < :endDate
              AND c.endDate > :startDate
              AND (:excludeId IS NULL OR c.id <> :excludeId)
            """)
    List<CampaignEntity> findConflicting(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate,
            @Param("excludeId") String excludeId);
}
