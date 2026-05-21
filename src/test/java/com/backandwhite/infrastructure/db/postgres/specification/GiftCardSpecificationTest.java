package com.backandwhite.infrastructure.db.postgres.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
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
class GiftCardSpecificationTest {

    @Mock
    private Root<GiftCardEntity> root;

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
    private Path statusPath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Path codePath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Expression lowerCode;

    @BeforeEach
    void setUp() {
        lenient().when(cb.conjunction()).thenReturn(basePredicate);
        lenient().when(cb.and(any(Predicate.class), any(Predicate.class))).thenReturn(combined);
    }

    @Test
    void withFilters_emptyMap_returnsConjunctionOnly() {
        Specification<GiftCardEntity> spec = GiftCardSpecification.withFilters(new HashMap<>());

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(basePredicate);
        verify(cb).conjunction();
        verify(cb, never()).and(any(Predicate.class), any(Predicate.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_status_addsEnumEqualPredicate() {
        when(root.get("status")).thenReturn(statusPath);
        when(cb.equal(statusPath, GiftCardStatus.ACTIVE)).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "ACTIVE");

        Predicate result = GiftCardSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(statusPath, GiftCardStatus.ACTIVE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_search_addsLikeOnLowerCode() {
        when(root.get("code")).thenReturn(codePath);
        when(cb.lower(codePath)).thenReturn(lowerCode);
        when(cb.like(lowerCode, "%gc-100%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", "GC-100");

        Predicate result = GiftCardSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).like(lowerCode, "%gc-100%");
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_allFilters_combinesAllPredicates() {
        when(root.get("status")).thenReturn(statusPath);
        when(root.get("code")).thenReturn(codePath);
        when(cb.equal(statusPath, GiftCardStatus.USED)).thenReturn(equalPredicate);
        when(cb.lower(codePath)).thenReturn(lowerCode);
        when(cb.like(lowerCode, "%xyz%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "USED");
        filters.put("search", "XYZ");

        Predicate result = GiftCardSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb, times(2)).and(any(Predicate.class), any(Predicate.class));
    }
}
