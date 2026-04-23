package com.backandwhite.infrastructure.message.kafka.producer;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.core.kafka.avro.*;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaCmsEventAdapter implements CmsEventPort {

    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    public void publishLoyaltyPointsEarned(String userId, String orderId, int pointsEarned, int totalPoints,
            String tier) {
        LoyaltyPointsEarnedEvent event = LoyaltyPointsEarnedEvent.newBuilder().setUserId(userId).setOrderId(orderId)
                .setPointsEarned(pointsEarned).setTotalPoints(totalPoints).setTier(tier).setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED, userId, event);
    }

    public void publishLoyaltyPointsRedeemed(String userId, int pointsRedeemed, int totalPoints, String couponCode,
            String discountAmount) {
        LoyaltyPointsRedeemedEvent event = LoyaltyPointsRedeemedEvent.newBuilder().setUserId(userId)
                .setPointsRedeemed(pointsRedeemed).setTotalPoints(totalPoints).setCouponCode(couponCode)
                .setDiscountAmount(discountAmount).setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_REDEEMED, userId, event);
    }

    public void publishGiftCardPurchased(String giftCardId, String code, String buyerId, String buyerName,
            String buyerEmail, String recipientName, String recipientEmail, String amount, String currency,
            String message, String expiryDate, String designId) {
        GiftCardPurchasedEvent event = GiftCardPurchasedEvent.newBuilder().setGiftCardId(giftCardId).setCode(code)
                .setBuyerId(buyerId).setBuyerName(buyerName).setBuyerEmail(buyerEmail).setRecipientName(recipientName)
                .setRecipientEmail(recipientEmail).setAmount(amount).setCurrency(currency).setMessage(message)
                .setExpiryDate(expiryDate).setDesignId(designId).setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_GIFT_CARD_PURCHASED, giftCardId, event);
    }

    public void publishGiftCardRedeemed(String giftCardId, String code, String userId, String amount,
            String remainingBalance, String orderId) {
        GiftCardRedeemedEvent event = GiftCardRedeemedEvent.newBuilder().setGiftCardId(giftCardId).setCode(code)
                .setUserId(userId).setAmount(amount).setRemainingBalance(remainingBalance).setOrderId(orderId)
                .setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_GIFT_CARD_REDEEMED, giftCardId, event);
    }

    public void publishNewsletterSubscribed(String email, String userId, String source) {
        NewsletterSubscribedEvent event = NewsletterSubscribedEvent.newBuilder().setEmail(email).setUserId(userId)
                .setSource(source != null ? source : "website").setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_SUBSCRIBED, email, event);
    }

    public void publishNewsletterUnsubscribed(String email, String userId) {
        NewsletterUnsubscribedEvent event = NewsletterUnsubscribedEvent.newBuilder().setEmail(email).setUserId(userId)
                .setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_UNSUBSCRIBED, email, event);
    }

    public void publishCurrencyRatesSynced(int ratesUpdated) {
        CurrencyRatesSyncedEvent event = CurrencyRatesSyncedEvent.newBuilder().setRatesUpdated(ratesUpdated)
                .setTimestamp(now()).build();
        send(AppConstants.KAFKA_TOPIC_CONFIG_CURRENCY_RATE_UPDATED, "currency-sync", event);
    }

    // ── Common ───────────────────────────────────────────────────────────────

    private void send(String topic, String key, SpecificRecord event) {
        log.info("::> Publishing to [{}] key={}: {}", topic, key, event.getClass().getSimpleName());
        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("::> Failed to publish to [{}]: {}", topic, ex.getMessage(), ex);
            } else {
                log.debug("::> Published to [{}] offset={}", topic, result.getRecordMetadata().offset());
            }
        });
    }

    private String now() {
        return Instant.now().toString();
    }
}
