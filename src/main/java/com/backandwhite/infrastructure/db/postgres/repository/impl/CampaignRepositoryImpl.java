package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.repository.CampaignRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.CampaignInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.CampaignJpaRepository;
import com.backandwhite.infrastructure.db.postgres.specification.CampaignSpecification;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepository {

    private final CampaignJpaRepository jpa;
    private final CampaignInfraMapper mapper;

    @Override
    public Campaign save(Campaign campaign) {
        campaign.setId(UUID.randomUUID().toString().toLowerCase());
        return mapper.toDomain(jpa.save(mapper.toEntity(campaign)));
    }

    @Override
    public Campaign update(Campaign campaign) {
        return mapper.toDomain(jpa.save(mapper.toEntity(campaign)));
    }

    @Override
    public Optional<Campaign> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Campaign> findAll(Map<String, Object> filters, Pageable pageable) {
        return jpa.findAll(CampaignSpecification.withFilters(filters), pageable).map(mapper::toDomain);
    }

    @Override
    public List<Campaign> findAllActive() {
        Instant now = Instant.now();
        return jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(now, now).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Campaign> findAllActive(String locale) {
        List<Campaign> list = findAllActive();
        if (locale == null || locale.isBlank() || list.isEmpty())
            return list;

        List<String> ids = list.stream().map(Campaign::getId).toList();
        Map<String, Object[]> translations = jpa.findTranslations(ids, locale).stream()
                .collect(java.util.stream.Collectors.toMap(r -> (String) r[0], r -> r));

        list.forEach(c -> {
            Object[] t = translations.get(c.getId());
            if (t == null)
                return;
            if (t[1] != null)
                c.setName((String) t[1]);
            if (t[2] != null)
                c.setBadge((String) t[2]);
            if (t[3] != null)
                c.setDescription((String) t[3]);
        });
        return list;
    }

    @Override
    public List<Campaign> findConflicting(Instant startDate, Instant endDate, String excludeId) {
        return jpa.findConflicting(startDate, endDate, excludeId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(String id) {
        jpa.deleteById(id);
    }
}
