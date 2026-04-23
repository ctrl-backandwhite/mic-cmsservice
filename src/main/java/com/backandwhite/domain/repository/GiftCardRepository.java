package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftCardRepository {
    // Designs
    GiftCardDesign saveDesign(GiftCardDesign design);

    GiftCardDesign updateDesign(GiftCardDesign design);

    Optional<GiftCardDesign> findDesignById(String id);

    List<GiftCardDesign> findAllActiveDesigns();

    List<GiftCardDesign> findAllDesigns();

    void deleteDesign(String id);

    // Gift Cards
    GiftCard save(GiftCard giftCard);

    GiftCard update(GiftCard giftCard);

    Optional<GiftCard> findById(String id);

    Optional<GiftCard> findByCode(String code);

    Page<GiftCard> findAll(Map<String, Object> filters, Pageable pageable);

    Page<GiftCard> findByBuyerId(String buyerId, Pageable pageable);

    Page<GiftCard> findByRecipientEmail(String email, Pageable pageable);

    List<GiftCard> findExpiredCards(LocalDate today);

    /**
     * Gift cards whose scheduled delivery instant has arrived and have not yet had
     * their Kafka purchase event published (email_sent = false and send_at is null
     * or already in the past). Consumed by the scheduled sender job. Uses
     * {@link java.time.Instant} so hour + minute precision is respected — a card
     * scheduled for "today at 11:43" must NOT fire at 11:00.
     */
    List<GiftCard> findPendingSends(java.time.Instant now);

    // Transactions
    GiftCardTransaction saveTransaction(GiftCardTransaction transaction);

    List<GiftCardTransaction> findTransactionsByGiftCardId(String giftCardId);
}
