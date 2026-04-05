package com.backandwhite.application.usecase;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.domain.model.Campaign;
import java.util.List;
import java.util.Map;

public interface CampaignUseCase {
    Campaign create(Campaign campaign);

    Campaign update(String id, Campaign campaign);

    Campaign findById(String id);

    PaginationDtoOut<Campaign> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending);

    List<Campaign> findAllActive();

    void delete(String id);

    void toggleActive(String id);
}
