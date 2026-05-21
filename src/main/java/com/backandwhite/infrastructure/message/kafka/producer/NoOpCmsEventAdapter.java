package com.backandwhite.infrastructure.message.kafka.producer;

import com.backandwhite.application.port.out.CmsEventPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Fallback adapter wired when Kafka is disabled (local dev / tests).
 *
 * <p>
 * Every method is intentionally a no-op — the event is silently dropped so the
 * rest of the use-case flow keeps working without a broker. Production uses
 * {@link KafkaCmsEventAdapter} instead.
 */
@Component
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpCmsEventAdapter implements CmsEventPort {

    @Override
    public void publishLoyaltyPointsEarned(String userId, String orderId, int pointsEarned, int totalPoints,
            String tier) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishLoyaltyPointsRedeemed(String userId, int pointsRedeemed, int totalPoints, String couponCode,
            String discountAmount) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishGiftCardPurchased(String giftCardId, String code, String buyerId, String buyerName,
            String buyerEmail, String recipientName, String recipientEmail, String amount, String currency,
            String message, String expiryDate, String designId) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishGiftCardRedeemed(String giftCardId, String code, String userId, String amount,
            String remainingBalance, String orderId) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishNewsletterSubscribed(String email, String userId, String source) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishNewsletterUnsubscribed(String email, String userId) {
        // Intentionally empty: Kafka disabled, event dropped.
    }

    @Override
    public void publishCurrencyRatesSynced(int ratesUpdated) {
        // Intentionally empty: Kafka disabled, event dropped.
    }
}
