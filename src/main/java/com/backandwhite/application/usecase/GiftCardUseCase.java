package com.backandwhite.application.usecase;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import java.util.List;
import java.util.Map;

public interface GiftCardUseCase {
    // Designs
    GiftCardDesign createDesign(GiftCardDesign design);

    GiftCardDesign updateDesign(String id, GiftCardDesign design);

    GiftCardDesign findDesignById(String id);

    List<GiftCardDesign> findAllDesigns();

    List<GiftCardDesign> findAllActiveDesigns();

    void deleteDesign(String id);

    // Gift Cards
    GiftCard purchase(GiftCard giftCard, String buyerEmail);

    GiftCard findById(String id);

    GiftCard findByCode(String code);

    PageResult<GiftCard> findAll(Map<String, Object> filters, int page, int size, String sortBy, boolean ascending);

    PageResult<GiftCard> findByBuyerId(String buyerId, int page, int size, String sortBy, boolean ascending);

    Money getBalance(String code);

    // My Gift Cards
    List<GiftCard> findMySent(String buyerId);

    List<GiftCard> findMyReceived(String recipientEmail);

    GiftCard claimByCode(String code, String userEmail);

    // Transactions
    GiftCardTransaction redeem(String code, Money amount, String orderId);

    List<GiftCardTransaction> findTransactions(String giftCardId);
}
