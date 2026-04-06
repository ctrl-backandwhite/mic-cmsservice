package com.backandwhite.application.port.out;

public interface CmsEventPort {

    void publishLoyaltyPointsEarned(String userId, String orderId,
            int pointsEarned, int totalPoints, String tier);

    void publishLoyaltyPointsRedeemed(String userId, int pointsRedeemed,
            int totalPoints, String couponCode, String discountAmount);

    void publishGiftCardPurchased(String giftCardId, String code, String buyerId,
            String buyerName, String recipientName, String recipientEmail,
            String amount, String currency, String message, String expiryDate, String designId);

    void publishGiftCardRedeemed(String giftCardId, String code, String userId,
            String amount, String remainingBalance, String orderId);

    void publishNewsletterSubscribed(String email, String userId, String source);

    void publishNewsletterUnsubscribed(String email, String userId);
}
