package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CampaignRepository {
    Campaign save(Campaign campaign);

    Campaign update(Campaign campaign);

    Optional<Campaign> findById(String id);

    Page<Campaign> findAll(Map<String, Object> filters, Pageable pageable);

    List<Campaign> findAllActive();

    void delete(String id);
}
