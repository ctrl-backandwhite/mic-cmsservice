package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.Campaign;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignRepository {
    Campaign save(Campaign campaign);

    Campaign update(Campaign campaign);

    Optional<Campaign> findById(String id);

    Page<Campaign> findAll(Map<String, Object> filters, Pageable pageable);

    List<Campaign> findAllActive();

    /**
     * Active campaigns with name/badge/description overlaid for the given locale.
     */
    List<Campaign> findAllActive(String locale);

    /**
     * Returns active campaigns whose date range overlaps [startDate, endDate),
     * excluding the campaign with the given excludeId (used for updates).
     */
    List<Campaign> findConflicting(Instant startDate, Instant endDate, String excludeId);

    void delete(String id);
}
