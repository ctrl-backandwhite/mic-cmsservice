package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardDesignEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardTransactionEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.GiftCardInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardDesignJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardTransactionJpaRepository;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class GiftCardRepositoryImplTest {

    @Mock
    private GiftCardDesignJpaRepository designJpa;

    @Mock
    private GiftCardJpaRepository cardJpa;

    @Mock
    private GiftCardTransactionJpaRepository transactionJpa;

    @Mock
    private GiftCardInfraMapper mapper;

    private GiftCardRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new GiftCardRepositoryImpl(designJpa, cardJpa, transactionJpa, mapper);
    }

    private GiftCard card() {
        return GiftCard.builder().code("ABC").build();
    }

    private GiftCardDesign design() {
        return GiftCardDesign.builder().name("d").build();
    }

    private GiftCardTransaction tx() {
        return GiftCardTransaction.builder().giftCardId("g1").build();
    }

    // Designs

    @Test
    void saveDesign_assignsId() {
        GiftCardDesign in = design();
        GiftCardDesignEntity entity = new GiftCardDesignEntity();
        GiftCardDesignEntity saved = new GiftCardDesignEntity();
        GiftCardDesign out = design();

        when(mapper.toDesignEntity(in)).thenReturn(entity);
        when(designJpa.save(entity)).thenReturn(saved);
        when(mapper.toDesignDomain(saved)).thenReturn(out);

        GiftCardDesign r = repo.saveDesign(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void updateDesign_keepsId() {
        GiftCardDesign in = design();
        in.setId("d1");
        GiftCardDesignEntity entity = new GiftCardDesignEntity();
        GiftCardDesignEntity saved = new GiftCardDesignEntity();
        GiftCardDesign out = design();

        when(mapper.toDesignEntity(in)).thenReturn(entity);
        when(designJpa.save(entity)).thenReturn(saved);
        when(mapper.toDesignDomain(saved)).thenReturn(out);

        assertThat(repo.updateDesign(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("d1");
    }

    @Test
    void findDesignById_present() {
        GiftCardDesignEntity e = new GiftCardDesignEntity();
        when(designJpa.findById("d1")).thenReturn(Optional.of(e));
        when(mapper.toDesignDomain(e)).thenReturn(design());
        assertThat(repo.findDesignById("d1")).isPresent();
    }

    @Test
    void findDesignById_empty() {
        when(designJpa.findById("d1")).thenReturn(Optional.empty());
        assertThat(repo.findDesignById("d1")).isEmpty();
    }

    @Test
    void findAllActiveDesigns_delegates() {
        GiftCardDesignEntity e = new GiftCardDesignEntity();
        when(designJpa.findByActiveTrue()).thenReturn(List.of(e));
        when(mapper.toDesignDomain(e)).thenReturn(design());
        assertThat(repo.findAllActiveDesigns()).hasSize(1);
    }

    @Test
    void findAllDesigns_delegates() {
        GiftCardDesignEntity e = new GiftCardDesignEntity();
        when(designJpa.findAll()).thenReturn(List.of(e));
        when(mapper.toDesignDomain(e)).thenReturn(design());
        assertThat(repo.findAllDesigns()).hasSize(1);
    }

    @Test
    void deleteDesign_delegates() {
        repo.deleteDesign("d1");
        verify(designJpa).deleteById("d1");
    }

    // Cards

    @Test
    void save_assignsId() {
        GiftCard in = card();
        GiftCardEntity entity = new GiftCardEntity();
        GiftCardEntity saved = new GiftCardEntity();
        GiftCard out = card();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(cardJpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        GiftCard r = repo.save(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void update_keepsId() {
        GiftCard in = card();
        in.setId("g1");
        GiftCardEntity entity = new GiftCardEntity();
        GiftCardEntity saved = new GiftCardEntity();
        GiftCard out = card();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(cardJpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
        assertThat(in.getId()).isEqualTo("g1");
    }

    @Test
    void findById_presentAndEmpty() {
        GiftCardEntity e = new GiftCardEntity();
        when(cardJpa.findById("g1")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(card());
        assertThat(repo.findById("g1")).isPresent();

        when(cardJpa.findById("g2")).thenReturn(Optional.empty());
        assertThat(repo.findById("g2")).isEmpty();
    }

    @Test
    void findByCode_present() {
        GiftCardEntity e = new GiftCardEntity();
        when(cardJpa.findByCode("ABC")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(card());
        assertThat(repo.findByCode("ABC")).isPresent();
    }

    @Test
    void findByCode_empty() {
        when(cardJpa.findByCode("X")).thenReturn(Optional.empty());
        assertThat(repo.findByCode("X")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_emptyFilters() {
        GiftCardEntity e = new GiftCardEntity();
        Page<GiftCardEntity> p = new PageImpl<>(List.of(e));
        when(cardJpa.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(card());

        assertThat(repo.findAll(Map.of(), PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_withAllFilters() {
        GiftCardEntity e = new GiftCardEntity();
        Page<GiftCardEntity> p = new PageImpl<>(List.of(e));
        when(cardJpa.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(card());

        Map<String, Object> filters = Map.of("status", "ACTIVE", "search", "abc");
        assertThat(repo.findAll(filters, PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void findByBuyerId_paged() {
        GiftCardEntity e = new GiftCardEntity();
        Page<GiftCardEntity> p = new PageImpl<>(List.of(e));
        when(cardJpa.findByBuyerId(eq("b1"), any(Pageable.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(card());

        assertThat(repo.findByBuyerId("b1", PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void findByRecipientEmail_paged() {
        GiftCardEntity e = new GiftCardEntity();
        Page<GiftCardEntity> p = new PageImpl<>(List.of(e));
        when(cardJpa.findByRecipientEmail(eq("a@a.com"), any(Pageable.class))).thenReturn(p);
        when(mapper.toDomain(e)).thenReturn(card());

        assertThat(repo.findByRecipientEmail("a@a.com", PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    @Test
    void findExpiredCards_delegates() {
        GiftCardEntity e = new GiftCardEntity();
        LocalDate today = LocalDate.now();
        when(cardJpa.findByStatusInAndExpiryDateBefore(List.of(GiftCardStatus.PENDING, GiftCardStatus.ACTIVE), today))
                .thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(card());

        assertThat(repo.findExpiredCards(today)).hasSize(1);
    }

    @Test
    void findPendingSends_delegates() {
        GiftCardEntity e = new GiftCardEntity();
        Instant now = Instant.now();
        when(cardJpa.findPendingSends(now)).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(card());

        assertThat(repo.findPendingSends(now)).hasSize(1);
    }

    // Transactions

    @Test
    void saveTransaction_assignsId() {
        GiftCardTransaction in = tx();
        GiftCardTransactionEntity entity = new GiftCardTransactionEntity();
        GiftCardTransactionEntity saved = new GiftCardTransactionEntity();
        GiftCardTransaction out = tx();

        when(mapper.toTransactionEntity(in)).thenReturn(entity);
        when(transactionJpa.save(entity)).thenReturn(saved);
        when(mapper.toTransactionDomain(saved)).thenReturn(out);

        GiftCardTransaction r = repo.saveTransaction(in);
        assertThat(in.getId()).isNotBlank();
        assertThat(r).isSameAs(out);
    }

    @Test
    void findTransactionsByGiftCardId_delegates() {
        GiftCardTransactionEntity e = new GiftCardTransactionEntity();
        when(transactionJpa.findByGiftCardIdOrderByCreatedAtDesc("g1")).thenReturn(List.of(e));
        when(mapper.toTransactionDomain(e)).thenReturn(tx());

        assertThat(repo.findTransactionsByGiftCardId("g1")).hasSize(1);
    }
}
