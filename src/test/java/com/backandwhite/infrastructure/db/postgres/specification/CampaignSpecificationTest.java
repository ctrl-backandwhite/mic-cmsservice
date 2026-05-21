package com.backandwhite.infrastructure.db.postgres.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.valueobject.CampaignType;
import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class CampaignSpecificationTest {

    @Mock
    private Root<CampaignEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Predicate basePredicate;

    @Mock
    private Predicate equalPredicate;

    @Mock
    private Predicate likePredicate;

    @Mock
    private Predicate combined;

    @Mock
    @SuppressWarnings("rawtypes")
    private Path activePath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Path typePath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Path namePath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Expression lowerName;

    @BeforeEach
    void setUp() {
        lenient().when(cb.conjunction()).thenReturn(basePredicate);
        lenient().when(cb.and(any(Predicate.class), any(Predicate.class))).thenReturn(combined);
    }

    @Test
    void withFilters_emptyMap_returnsConjunctionOnly() {
        Specification<CampaignEntity> spec = CampaignSpecification.withFilters(new HashMap<>());

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(basePredicate);
        verify(cb).conjunction();
        verify(cb, never()).and(any(Predicate.class), any(Predicate.class));
        verify(cb, never()).equal(any(), any());
        verify(cb, never()).like(any(), any(String.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_active_addsEqualPredicate() {
        when(root.get("active")).thenReturn(activePath);
        when(cb.equal(activePath, Boolean.TRUE)).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("active", "true");

        Predicate result = CampaignSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(activePath, Boolean.TRUE);
        verify(cb).and(basePredicate, equalPredicate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_type_addsEnumEqualPredicate() {
        when(root.get("type")).thenReturn(typePath);
        when(cb.equal(typePath, CampaignType.PERCENTAGE)).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("type", "PERCENTAGE");

        Predicate result = CampaignSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(typePath, CampaignType.PERCENTAGE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_search_addsLikeOnLowerName() {
        when(root.get("name")).thenReturn(namePath);
        when(cb.lower(namePath)).thenReturn(lowerName);
        when(cb.like(lowerName, "%black%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", "Black");

        Predicate result = CampaignSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).lower(namePath);
        verify(cb).like(lowerName, "%black%");
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_allFilters_combinesAllPredicates() {
        when(root.get("active")).thenReturn(activePath);
        when(root.get("type")).thenReturn(typePath);
        when(root.get("name")).thenReturn(namePath);
        when(cb.equal(activePath, Boolean.FALSE)).thenReturn(equalPredicate);
        when(cb.equal(typePath, CampaignType.FLASH)).thenReturn(equalPredicate);
        when(cb.lower(namePath)).thenReturn(lowerName);
        when(cb.like(lowerName, "%sale%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("active", "false");
        filters.put("type", "FLASH");
        filters.put("search", "SALE");

        Predicate result = CampaignSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb, times(3)).and(any(Predicate.class), any(Predicate.class));
    }
}
