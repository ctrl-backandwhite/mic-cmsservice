package com.backandwhite.application.usecase.impl;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.application.usecase.CampaignUseCase;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CampaignUseCaseImpl implements CampaignUseCase {

    private final CampaignRepository campaignRepository;

    @Override
    @Transactional
    public Campaign create(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    @Transactional
    public Campaign update(String id, Campaign campaign) {
        campaignRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        campaign.setId(id);
        return campaignRepository.update(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public Campaign findById(String id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Campaign> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(campaignRepository.findAll(filters, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Campaign> findAllActive() {
        return campaignRepository.findAllActive();
    }

    @Override
    @Transactional
    public void delete(String id) {
        campaignRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        campaignRepository.delete(id);
    }

    @Override
    @Transactional
    public void toggleActive(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        campaign.setActive(!campaign.isActive());
        campaignRepository.update(campaign);
    }
}
