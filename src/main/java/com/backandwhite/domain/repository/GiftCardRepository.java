package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // Transactions
    GiftCardTransaction saveTransaction(GiftCardTransaction transaction);

    List<GiftCardTransaction> findTransactionsByGiftCardId(String giftCardId);
}
