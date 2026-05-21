package com.backandwhite.infrastructure.db.postgres.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.valueobject.NewsletterStatus;
import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
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
class NewsletterSpecificationTest {

    @Mock
    private Root<NewsletterSubscriberEntity> root;

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
    private Path emailPath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Expression lowerEmail;

    @BeforeEach
    void setUp() {
        lenient().when(cb.conjunction()).thenReturn(basePredicate);
        lenient().when(cb.and(any(Predicate.class), any(Predicate.class))).thenReturn(combined);
    }

    @Test
    void withFilters_emptyMap_returnsConjunctionOnly() {
        Specification<NewsletterSubscriberEntity> spec = NewsletterSpecification.withFilters(new HashMap<>());

        Predicate result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(basePredicate);
        verify(cb).conjunction();
        verify(cb, never()).and(any(Predicate.class), any(Predicate.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_status_addsEnumEqualPredicate() {
        when(root.get("status")).thenReturn(statusPath);
        when(cb.equal(statusPath, NewsletterStatus.ACTIVE)).thenReturn(equalPredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "ACTIVE");

        Predicate result = NewsletterSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).equal(statusPath, NewsletterStatus.ACTIVE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_search_addsLikeOnLowerEmail() {
        when(root.get("email")).thenReturn(emailPath);
        when(cb.lower(emailPath)).thenReturn(lowerEmail);
        when(cb.like(lowerEmail, "%foo@bar%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", "FOO@BAR");

        Predicate result = NewsletterSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb).like(lowerEmail, "%foo@bar%");
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_allFilters_combinesAllPredicates() {
        when(root.get("status")).thenReturn(statusPath);
        when(root.get("email")).thenReturn(emailPath);
        when(cb.equal(statusPath, NewsletterStatus.UNSUBSCRIBED)).thenReturn(equalPredicate);
        when(cb.lower(emailPath)).thenReturn(lowerEmail);
        when(cb.like(lowerEmail, "%@gmail%")).thenReturn(likePredicate);

        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "UNSUBSCRIBED");
        filters.put("search", "@GMAIL");

        Predicate result = NewsletterSpecification.withFilters(filters).toPredicate(root, query, cb);

        assertThat(result).isSameAs(combined);
        verify(cb, times(2)).and(any(Predicate.class), any(Predicate.class));
    }
}
