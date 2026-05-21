package com.backandwhite.application.port.out;

public interface CmsEventPort {

    void publishLoyaltyPointsEarned(String userId, String orderId, int pointsEarned, int totalPoints, String tier);

    void publishLoyaltyPointsRedeemed(String userId, int pointsRedeemed, int totalPoints, String couponCode,
            String discountAmount);

    /**
     * Publishes a {@code gift_card.purchased} event. Has 12 String parameters by
     * design — every field is part of the public Kafka contract consumed by the
     * notification, order and analytics services. Refactoring to a DTO would break
     * all downstream listeners, so we suppress S107 here.
     */
    @SuppressWarnings("java:S107")
    void publishGiftCardPurchased(String giftCardId, String code, String buyerId, String buyerName, String buyerEmail,
            String recipientName, String recipientEmail, String amount, String currency, String message,
            String expiryDate, String designId);

    void publishGiftCardRedeemed(String giftCardId, String code, String userId, String amount, String remainingBalance,
            String orderId);

    void publishNewsletterSubscribed(String email, String userId, String source);

    void publishNewsletterUnsubscribed(String email, String userId);

    void publishCurrencyRatesSynced(int ratesUpdated);
}
