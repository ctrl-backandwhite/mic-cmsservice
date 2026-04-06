package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.repository.GiftCardRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.GiftCardInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardDesignJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardJpaRepository;
import com.backandwhite.infrastructure.db.postgres.repository.GiftCardTransactionJpaRepository;
import com.backandwhite.infrastructure.db.postgres.specification.GiftCardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GiftCardRepositoryImpl implements GiftCardRepository {

    private final GiftCardDesignJpaRepository designJpa;
    private final GiftCardJpaRepository cardJpa;
    private final GiftCardTransactionJpaRepository transactionJpa;
    private final GiftCardInfraMapper mapper;

    // Designs

    @Override
    public GiftCardDesign saveDesign(GiftCardDesign design) {
        design.setId(UUID.randomUUID().toString());
        return mapper.toDesignDomain(designJpa.save(mapper.toDesignEntity(design)));
    }

    @Override
    public GiftCardDesign updateDesign(GiftCardDesign design) {
        return mapper.toDesignDomain(designJpa.save(mapper.toDesignEntity(design)));
    }

    @Override
    public Optional<GiftCardDesign> findDesignById(String id) {
        return designJpa.findById(id).map(mapper::toDesignDomain);
    }

    @Override
    public List<GiftCardDesign> findAllActiveDesigns() {
        return designJpa.findByActiveTrue().stream()
                .map(mapper::toDesignDomain)
                .toList();
    }

    @Override
    public List<GiftCardDesign> findAllDesigns() {
        return designJpa.findAll().stream()
                .map(mapper::toDesignDomain)
                .toList();
    }

    @Override
    public void deleteDesign(String id) {
        designJpa.deleteById(id);
    }

    // Gift Cards

    @Override
    public GiftCard save(GiftCard giftCard) {
        giftCard.setId(UUID.randomUUID().toString());
        return mapper.toDomain(cardJpa.save(mapper.toEntity(giftCard)));
    }

    @Override
    public GiftCard update(GiftCard giftCard) {
        return mapper.toDomain(cardJpa.save(mapper.toEntity(giftCard)));
    }

    @Override
    public Optional<GiftCard> findById(String id) {
        return cardJpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<GiftCard> findByCode(String code) {
        return cardJpa.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public Page<GiftCard> findAll(Map<String, Object> filters, Pageable pageable) {
        return cardJpa.findAll(GiftCardSpecification.withFilters(filters), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<GiftCard> findByBuyerId(String buyerId, Pageable pageable) {
        return cardJpa.findByBuyerId(buyerId, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<GiftCard> findByRecipientEmail(String email, Pageable pageable) {
        return cardJpa.findByRecipientEmail(email, pageable).map(mapper::toDomain);
    }

    @Override
    public List<GiftCard> findExpiredCards(java.time.LocalDate today) {
        return cardJpa.findByStatusInAndExpiryDateBefore(
                java.util.List.of(
                        com.backandwhite.domain.valueobject.GiftCardStatus.PENDING,
                        com.backandwhite.domain.valueobject.GiftCardStatus.ACTIVE),
                today)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    // Transactions

    @Override
    public GiftCardTransaction saveTransaction(GiftCardTransaction transaction) {
        transaction.setId(UUID.randomUUID().toString());
        return mapper.toTransactionDomain(transactionJpa.save(mapper.toTransactionEntity(transaction)));
    }

    @Override
    public List<GiftCardTransaction> findTransactionsByGiftCardId(String giftCardId) {
        return transactionJpa.findByGiftCardIdOrderByCreatedAtDesc(giftCardId).stream()
                .map(mapper::toTransactionDomain)
                .toList();
    }
}
