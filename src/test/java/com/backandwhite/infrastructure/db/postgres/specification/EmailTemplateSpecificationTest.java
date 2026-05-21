package com.backandwhite.infrastructure.db.postgres.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.valueobject.EmailTrigger;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
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
class EmailTemplateSpecificationTest {

    @Mock
    private Root<EmailTemplateEntity> root;

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
    private Path categoryPath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Path triggerPath;

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
        Specification<EmailTemplateEntity> spec = EmailTemplateSpecification.withFilters(new HashMap<>());

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(basePredicate);
        verify(cb).conjunction();
        verify(cb, never()).and(any(Predicate.class), any(Predicate.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_category_addsEqualPredicate() {
        when(root.get("category")).thenReturn(categoryPath);
        when(cb.equal(categoryPath, "transactional")).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("category", "transactional");

        Predicate result = EmailTemplateSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(categoryPath, "transactional");
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_triggerType_addsEnumEqualPredicate() {
        when(root.get("triggerType")).thenReturn(triggerPath);
        when(cb.equal(triggerPath, EmailTrigger.ORDER_CONFIRMED)).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("triggerType", "ORDER_CONFIRMED");

        Predicate result = EmailTemplateSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(triggerPath, EmailTrigger.ORDER_CONFIRMED);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_search_addsLikeOnLowerName() {
        when(root.get("name")).thenReturn(namePath);
        when(cb.lower(namePath)).thenReturn(lowerName);
        when(cb.like(lowerName, "%welcome%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", "Welcome");

        Predicate result = EmailTemplateSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).like(lowerName, "%welcome%");
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_allFilters_combinesAllPredicates() {
        when(root.get("category")).thenReturn(categoryPath);
        when(root.get("triggerType")).thenReturn(triggerPath);
        when(root.get("name")).thenReturn(namePath);
        when(cb.equal(categoryPath, "marketing")).thenReturn(equalPredicate);
        when(cb.equal(triggerPath, EmailTrigger.WELCOME)).thenReturn(equalPredicate);
        when(cb.lower(namePath)).thenReturn(lowerName);
        when(cb.like(lowerName, "%hello%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("category", "marketing");
        filters.put("triggerType", "WELCOME");
        filters.put("search", "Hello");

        Predicate result = EmailTemplateSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb, times(3)).and(any(Predicate.class), any(Predicate.class));
    }
}
