package com.backandwhite.application.usecase.impl;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

import com.backandwhite.application.port.out.CatalogPort;
import com.backandwhite.application.usecase.CampaignUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.exception.BusinessException;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.repository.CampaignRepository;
import com.backandwhite.domain.valueobject.CampaignType;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignUseCaseImpl implements CampaignUseCase {

    private final CampaignRepository campaignRepository;
    private final CatalogPort catalogPort;

    private enum CampaignScope {
        ALL, CATEGORIES, PRODUCTS
    }

    private CampaignScope resolveScope(Campaign c) {
        if (c.getAppliesToCategories() != null && !c.getAppliesToCategories().isEmpty())
            return CampaignScope.CATEGORIES;
        if (c.getAppliesToProducts() != null && !c.getAppliesToProducts().isEmpty())
            return CampaignScope.PRODUCTS;
        return CampaignScope.ALL;
    }

    private void checkOverlap(Campaign campaign) {
        List<Campaign> conflicting = campaignRepository.findConflicting(campaign.getStartDate(), campaign.getEndDate(),
                campaign.getId());
        if (conflicting.isEmpty())
            return;

        boolean newIsFS = campaign.getType() == CampaignType.FREE_SHIPPING;
        CampaignScope newScope = resolveScope(campaign);

        for (Campaign existing : conflicting) {
            boolean existingIsFS = existing.getType() == CampaignType.FREE_SHIPPING;
            if (newIsFS != existingIsFS)
                continue;

            CampaignScope existingScope = resolveScope(existing);

            if (newScope == CampaignScope.ALL || existingScope == CampaignScope.ALL) {
                throw new BusinessException("OV001", "Conflicto con campana '" + existing.getName()
                        + "': una campana global no puede coexistir con otra en el mismo periodo");
            }

            if (newScope == CampaignScope.CATEGORIES && existingScope == CampaignScope.CATEGORIES) {
                Set<String> expandedNew = catalogPort.expandWithSubcategories(campaign.getAppliesToCategories());
                Set<String> expandedExisting = catalogPort.expandWithSubcategories(existing.getAppliesToCategories());
                Set<String> intersection = new HashSet<>(expandedNew);
                intersection.retainAll(expandedExisting);
                if (!intersection.isEmpty()) {
                    throw new BusinessException("OV002", "Conflicto con campana '" + existing.getName()
                            + "': categorias solapadas (incluye subcategorias)");
                }
            }

            if (newScope == CampaignScope.PRODUCTS && existingScope == CampaignScope.PRODUCTS) {
                Set<String> intersection = new HashSet<>(campaign.getAppliesToProducts());
                intersection.retainAll(existing.getAppliesToProducts());
                if (!intersection.isEmpty()) {
                    throw new BusinessException("OV003",
                            "Conflicto con campana '" + existing.getName() + "': productos en comun");
                }
            }

            if (newScope == CampaignScope.CATEGORIES && existingScope == CampaignScope.PRODUCTS) {
                Set<String> expandedCats = catalogPort.expandWithSubcategories(campaign.getAppliesToCategories());
                Map<String, String> prodCatMap = catalogPort.getProductCategoryMap(existing.getAppliesToProducts());
                boolean conflict = prodCatMap.values().stream().anyMatch(expandedCats::contains);
                if (conflict) {
                    throw new BusinessException("OV004", "Conflicto cruzado con campana '" + existing.getName()
                            + "': productos de otra campana pertenecen a categorias afectadas");
                }
            }

            if (newScope == CampaignScope.PRODUCTS && existingScope == CampaignScope.CATEGORIES) {
                Set<String> expandedCats = catalogPort.expandWithSubcategories(existing.getAppliesToCategories());
                Map<String, String> prodCatMap = catalogPort.getProductCategoryMap(campaign.getAppliesToProducts());
                boolean conflict = prodCatMap.values().stream().anyMatch(expandedCats::contains);
                if (conflict) {
                    throw new BusinessException("OV004", "Conflicto cruzado con campana '" + existing.getName()
                            + "': productos afectados pertenecen a categorias de otra campana");
                }
            }
        }
    }

    @Override
    @Transactional
    public Campaign create(Campaign campaign) {
        validateCampaign(campaign);
        checkOverlap(campaign);
        return campaignRepository.save(campaign);
    }

    @Override
    @Transactional
    public Campaign update(String id, Campaign campaign) {
        campaignRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        campaign.setId(id);
        validateCampaign(campaign);
        checkOverlap(campaign);
        return campaignRepository.update(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public Campaign findById(String id) {
        return campaignRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
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
        campaignRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        campaignRepository.delete(id);
    }

    @Override
    @Transactional
    public void toggleActive(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Campaign", id));
        boolean wasActive = campaign.isActive();
        campaign.setActive(!wasActive);
        if (!wasActive) {
            checkOverlap(campaign);
        }
        campaignRepository.update(campaign);
    }

    private void validateCampaign(Campaign c) {
        if (c.getStartDate() != null && c.getEndDate() != null && !c.getEndDate().isAfter(c.getStartDate())) {
            throw new BusinessException("VE002", "endDate must be after startDate");
        }

        var type = c.getType();
        BigDecimal value = c.getValue() != null ? c.getValue().getAmount() : null;

        switch (type) {
            case PERCENTAGE, FLASH -> {
                if (value == null || value.compareTo(BigDecimal.ZERO) <= 0
                        || value.compareTo(BigDecimal.valueOf(100)) > 0) {
                    throw new BusinessException("VE003",
                            type + " campaign value must be between 0 and 100 (percentage)");
                }
            }
            case FIXED -> {
                if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException("VE004", "FIXED campaign value must be > 0");
                }
            }
            case BUNDLE -> {
                if (c.getBuyQty() == null || c.getBuyQty() <= 0) {
                    throw new BusinessException("VE005", "BUNDLE campaign requires buyQty > 0");
                }
                if (c.getGetQty() == null || c.getGetQty() <= 0) {
                    throw new BusinessException("VE006", "BUNDLE campaign requires getQty > 0");
                }
            }
            case BUY2GET1 -> {
            }
            case FREE_SHIPPING -> {
            }
        }
    }
}
