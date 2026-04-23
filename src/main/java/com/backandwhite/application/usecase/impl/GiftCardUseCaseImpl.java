package com.backandwhite.application.usecase.impl;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;
import static com.backandwhite.domain.exception.Message.*;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.application.usecase.GiftCardUseCase;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.repository.GiftCardRepository;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GiftCardUseCaseImpl implements GiftCardUseCase {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int MAX_CODE_RETRIES = 10;

    private final GiftCardRepository giftCardRepository;
    private final CmsEventPort cmsEventPort;

    // Designs

    @Override
    @Transactional
    public GiftCardDesign createDesign(GiftCardDesign design) {
        return giftCardRepository.saveDesign(design);
    }

    @Override
    @Transactional
    public GiftCardDesign updateDesign(String id, GiftCardDesign design) {
        giftCardRepository.findDesignById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCardDesign", id));
        design.setId(id);
        return giftCardRepository.updateDesign(design);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCardDesign findDesignById(String id) {
        return giftCardRepository.findDesignById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCardDesign", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardDesign> findAllDesigns() {
        return giftCardRepository.findAllDesigns();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardDesign> findAllActiveDesigns() {
        return giftCardRepository.findAllActiveDesigns();
    }

    @Override
    @Transactional
    public void deleteDesign(String id) {
        giftCardRepository.findDesignById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCardDesign", id));
        giftCardRepository.deleteDesign(id);
    }

    // Gift Cards

    @Override
    @Transactional
    public GiftCard purchase(GiftCard giftCard, String buyerEmail) {
        giftCard.setCode(generateCode());
        giftCard.setBalance(giftCard.getOriginalAmount());
        giftCard.setStatus(GiftCardStatus.PENDING);
        if (giftCard.getExpiryDate() == null) {
            giftCard.setExpiryDate(LocalDate.now().plusYears(1));
        }
        GiftCard saved = giftCardRepository.save(giftCard);

        giftCardRepository.saveTransaction(
                GiftCardTransaction.builder().giftCardId(saved.getId()).type(GiftCardTransactionType.PURCHASE)
                        .amount(saved.getOriginalAmount()).createdAt(Instant.now()).build());

        cmsEventPort.publishGiftCardPurchased(saved.getId(), saved.getCode(), saved.getBuyerId(), null, buyerEmail,
                saved.getRecipientName(), saved.getRecipientEmail(), saved.getOriginalAmount().toPlainString(), "USD",
                saved.getMessage(), saved.getExpiryDate() != null ? saved.getExpiryDate().toString() : null,
                saved.getDesignId());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCard findById(String id) {
        return giftCardRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCard", id));
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCard findByCode(String code) {
        return giftCardRepository.findByCode(code)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCard", code));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<GiftCard> findAll(Map<String, Object> filters, int page, int size, String sortBy,
            boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(giftCardRepository.findAll(filters, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<GiftCard> findByBuyerId(String buyerId, int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(giftCardRepository.findByBuyerId(buyerId, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public Money getBalance(String code) {
        GiftCard card = giftCardRepository.findByCode(code)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCard", code));
        return card.getBalance();
    }

    // My Gift Cards

    @Override
    @Transactional(readOnly = true)
    public List<GiftCard> findMySent(String buyerId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdAt").descending());
        return giftCardRepository.findByBuyerId(buyerId, pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCard> findMyReceived(String recipientEmail) {
        if (recipientEmail == null || recipientEmail.isBlank())
            return List.of();
        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdAt").descending());
        return giftCardRepository.findByRecipientEmail(recipientEmail, pageable).getContent();
    }

    @Override
    @Transactional
    public GiftCard claimByCode(String code, String userEmail) {
        GiftCard card = giftCardRepository.findByCode(code)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCard", code));
        if (card.getStatus() != GiftCardStatus.PENDING && card.getStatus() != GiftCardStatus.ACTIVE) {
            throw GIFT_CARD_INACTIVE.toBusinessException();
        }
        // Already fully activated — nothing to do
        if (card.getActivatedAt() != null && card.getRecipientEmail() != null && !card.getRecipientEmail().isBlank()) {
            throw GIFT_CARD_ALREADY_ACTIVATED.toBusinessException();
        }
        boolean changed = false;
        // Associate the card with the claiming user's email if not already set
        if (card.getRecipientEmail() == null || card.getRecipientEmail().isBlank()) {
            card.setRecipientEmail(userEmail);
            changed = true;
        }
        // Mark as activated on first claim
        if (card.getActivatedAt() == null) {
            card.setStatus(GiftCardStatus.ACTIVE);
            card.setActivatedAt(Instant.now());
            changed = true;
        }
        if (changed) {
            card.setUpdatedAt(Instant.now());
            return giftCardRepository.update(card);
        }
        return card;
    }

    // Transactions

    @Override
    @Transactional
    public GiftCardTransaction redeem(String code, Money amount, String orderId) {
        GiftCard card = giftCardRepository.findByCode(code)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("GiftCard", code));

        if (card.getStatus() != GiftCardStatus.ACTIVE) {
            throw GIFT_CARD_INACTIVE.toBusinessException();
        }
        if (card.getBalance().isLessThan(amount)) {
            throw GIFT_CARD_INSUFFICIENT_BALANCE.toBusinessException(card.getBalance().toPlainString());
        }

        card.setBalance(card.getBalance().subtract(amount));
        if (card.getBalance().isZero()) {
            card.setStatus(GiftCardStatus.USED);
        }
        giftCardRepository.update(card);

        GiftCardTransaction tx = giftCardRepository.saveTransaction(
                GiftCardTransaction.builder().giftCardId(card.getId()).type(GiftCardTransactionType.REDEEM)
                        .amount(amount.negate()).orderId(orderId).createdAt(Instant.now()).build());

        final String cardId = card.getId();
        final String cardCode = code;
        final String buyerId = card.getBuyerId();
        cmsEventPort.publishGiftCardRedeemed(cardId, cardCode, buyerId, amount.toPlainString(),
                card.getBalance().toPlainString(), orderId);

        return tx;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardTransaction> findTransactions(String giftCardId) {
        return giftCardRepository.findTransactionsByGiftCardId(giftCardId);
    }

    private String generateCode() {
        for (int attempt = 0; attempt < MAX_CODE_RETRIES; attempt++) {
            String code = "GC-" + randomBlock(4) + "-" + randomBlock(4) + "-" + randomBlock(4);
            if (giftCardRepository.findByCode(code).isEmpty()) {
                return code;
            }
        }
        throw new IllegalStateException(
                "Unable to generate unique gift card code after " + MAX_CODE_RETRIES + " attempts");
    }

    private String randomBlock(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }
}
