package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.Campaign;
import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.CampaignInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.CampaignJpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class CampaignRepositoryImplTest {

    @Mock
    private CampaignJpaRepository jpa;

    @Mock
    private CampaignInfraMapper mapper;

    private CampaignRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new CampaignRepositoryImpl(jpa, mapper);
    }

    private Campaign camp(String id) {
        return Campaign.builder().id(id).name("c").active(true).build();
    }

    @Test
    void save_assignsLowercaseId() {
        Campaign in = camp(null);
        CampaignEntity entity = new CampaignEntity();
        CampaignEntity saved = new CampaignEntity();
        Campaign out = camp("x");

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        Campaign result = repo.save(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(in.getId()).isEqualTo(in.getId().toLowerCase());
        assertThat(result).isSameAs(out);
    }

    @Test
    void update_keepsId() {
        Campaign in = camp("kept");
        CampaignEntity entity = new CampaignEntity();
        CampaignEntity saved = new CampaignEntity();
        Campaign out = camp("kept");

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("kept");
    }

    @Test
    void findById_present() {
        CampaignEntity e = new CampaignEntity();
        when(jpa.findById("c1")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        assertThat(repo.findById("c1")).isPresent();
    }

    @Test
    void findById_empty() {
        when(jpa.findById("c1")).thenReturn(Optional.empty());
        assertThat(repo.findById("c1")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_emptyFilters() {
        CampaignEntity e = new CampaignEntity();
        Page<CampaignEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        assertThat(repo.findAll(Map.of(), PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_withAllFilters() {
        CampaignEntity e = new CampaignEntity();
        Page<CampaignEntity> p = new PageImpl<>(List.of(e));
        when(jpa.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        Map<String, Object> filters = Map.of("active", "true", "type", "PERCENTAGE", "search", "Black");
        assertThat(repo.findAll(filters, PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void findAllActive_noArgs() {
        CampaignEntity e = new CampaignEntity();
        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        assertThat(repo.findAllActive()).hasSize(1);
    }

    @Test
    void findAllActive_localeNull_returnsBase() {
        CampaignEntity e = new CampaignEntity();
        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        List<Campaign> r = repo.findAllActive(null);
        assertThat(r).hasSize(1);
    }

    @Test
    void findAllActive_localeBlank_returnsBase() {
        CampaignEntity e = new CampaignEntity();
        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        assertThat(repo.findAllActive("   ")).hasSize(1);
    }

    @Test
    void findAllActive_emptyList_returnsImmediately() {
        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of());

        assertThat(repo.findAllActive("es")).isEmpty();
    }

    @Test
    void findAllActive_localeWithTranslations_overlays() {
        CampaignEntity e = new CampaignEntity();
        Campaign c = camp("c1");
        c.setName("orig");
        c.setBadge("ob");
        c.setDescription("od");

        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(c);

        Object[] row = new Object[]{"c1", "translatedName", "translatedBadge", "translatedDesc"};
        when(jpa.findTranslations(List.of("c1"), "es")).thenReturn(List.<Object[]>of(row));

        List<Campaign> r = repo.findAllActive("es");
        assertThat(r).hasSize(1);
        assertThat(r.get(0).getName()).isEqualTo("translatedName");
        assertThat(r.get(0).getBadge()).isEqualTo("translatedBadge");
        assertThat(r.get(0).getDescription()).isEqualTo("translatedDesc");
    }

    @Test
    void findAllActive_localeWithMissingTranslation_skipsCampaign() {
        CampaignEntity e = new CampaignEntity();
        Campaign c = camp("c1");
        c.setName("orig");

        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(c);
        when(jpa.findTranslations(List.of("c1"), "es")).thenReturn(List.of());

        List<Campaign> r = repo.findAllActive("es");
        assertThat(r).hasSize(1);
        assertThat(r.get(0).getName()).isEqualTo("orig");
    }

    @Test
    void findAllActive_localeWithPartialNullColumns_keepsOriginals() {
        CampaignEntity e = new CampaignEntity();
        Campaign c = camp("c1");
        c.setName("orig");
        c.setBadge("ob");
        c.setDescription("od");

        when(jpa.findByActiveTrueAndStartDateBeforeAndEndDateAfter(any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(c);

        Object[] row = new Object[]{"c1", null, null, null};
        when(jpa.findTranslations(List.of("c1"), "es")).thenReturn(List.<Object[]>of(row));

        List<Campaign> r = repo.findAllActive("es");
        assertThat(r.get(0).getName()).isEqualTo("orig");
        assertThat(r.get(0).getBadge()).isEqualTo("ob");
        assertThat(r.get(0).getDescription()).isEqualTo("od");
    }

    @Test
    void findConflicting_delegates() {
        CampaignEntity e = new CampaignEntity();
        Instant start = Instant.parse("2025-01-01T00:00:00Z");
        Instant end = Instant.parse("2025-02-01T00:00:00Z");
        when(jpa.findConflicting(start, end, "ex")).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(camp("c1"));

        assertThat(repo.findConflicting(start, end, "ex")).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("id");
        verify(jpa).deleteById("id");
    }
}
