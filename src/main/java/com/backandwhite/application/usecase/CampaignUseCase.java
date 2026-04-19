package com.backandwhite.application.usecase;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.domain.model.Campaign;
import java.util.List;
import java.util.Map;

public interface CampaignUseCase {
    Campaign create(Campaign campaign);

    Campaign update(String id, Campaign campaign);

    Campaign findById(String id);

    PageResult<Campaign> findAll(Map<String, Object> filters, int page, int size, String sortBy, boolean ascending);

    List<Campaign> findAllActive();

    /**
     * Active campaigns with name/badge/description translated to the given locale.
     */
    List<Campaign> findAllActive(String locale);

    void delete(String id);

    void toggleActive(String id);
}
