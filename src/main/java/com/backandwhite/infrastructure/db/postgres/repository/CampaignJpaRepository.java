package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface CampaignJpaRepository
        extends JpaRepository<CampaignEntity, String>,
        JpaSpecificationExecutor<CampaignEntity> {

    List<CampaignEntity> findByActiveTrueAndStartDateBeforeAndEndDateAfter(Instant now1, Instant now2);
}
