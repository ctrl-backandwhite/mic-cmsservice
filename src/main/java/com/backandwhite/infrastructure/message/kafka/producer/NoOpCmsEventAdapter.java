package com.backandwhite.infrastructure.message.kafka.producer;

import com.backandwhite.application.port.out.CmsEventPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpCmsEventAdapter implements CmsEventPort {

    @Override
    public void publishLoyaltyPointsEarned(String userId, String orderId,
            int pointsEarned, int totalPoints, String tier) {
    }

    @Override
    public void publishLoyaltyPointsRedeemed(String userId, int pointsRedeemed,
            int totalPoints, String couponCode, String discountAmount) {
    }

    @Override
    public void publishGiftCardPurchased(String giftCardId, String code, String buyerId,
            String buyerName, String recipientName, String recipientEmail,
            String amount, String currency, String message, String expiryDate, String designId) {
    }

    @Override
    public void publishGiftCardRedeemed(String giftCardId, String code, String userId,
            String amount, String remainingBalance, String orderId) {
    }

    @Override
    public void publishNewsletterSubscribed(String email, String userId, String source) {
    }

    @Override
    public void publishNewsletterUnsubscribed(String email, String userId) {
    }

    @Override
    public void publishCurrencyRatesSynced(int ratesUpdated) {
    }
}
