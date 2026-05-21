package com.backandwhite.infrastructure.message.kafka.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.constants.AppConstants;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class KafkaCmsEventAdapterTest {

    @Mock
    private KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    @InjectMocks
    private KafkaCmsEventAdapter adapter;

    private CompletableFuture<SendResult<String, SpecificRecord>> stubFuture(String topic, String key) {
        CompletableFuture<SendResult<String, SpecificRecord>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq(topic), eq(key), any(SpecificRecord.class))).thenReturn(future);
        return future;
    }

    @Test
    void publishLoyaltyPointsEarned_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED, "user-1");

        adapter.publishLoyaltyPointsEarned("user-1", "order-1", 100, 500, "GOLD");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED), eq("user-1"),
                any(SpecificRecord.class));
    }

    @Test
    void publishLoyaltyPointsEarned_whenSuccess_completesFuture() {
        CompletableFuture<SendResult<String, SpecificRecord>> future = stubFuture(
                AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED, "user-1");

        adapter.publishLoyaltyPointsEarned("user-1", "order-1", 10, 50, "SILVER");

        @SuppressWarnings("unchecked")
        SendResult<String, SpecificRecord> sendResult = mock(SendResult.class);
        RecordMetadata meta = new RecordMetadata(new TopicPartition("t", 0), 0L, 0, 0L, 0, 0);
        when(sendResult.getRecordMetadata()).thenReturn(meta);
        future.complete(sendResult);

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED), eq("user-1"),
                any(SpecificRecord.class));
    }

    @Test
    void publishLoyaltyPointsEarned_whenFailure_logsError() {
        CompletableFuture<SendResult<String, SpecificRecord>> future = stubFuture(
                AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED, "user-1");

        adapter.publishLoyaltyPointsEarned("user-1", "order-1", 10, 50, "SILVER");

        future.completeExceptionally(new RuntimeException("kafka down"));

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_EARNED), eq("user-1"),
                any(SpecificRecord.class));
    }

    @Test
    void publishLoyaltyPointsRedeemed_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_REDEEMED, "user-2");

        adapter.publishLoyaltyPointsRedeemed("user-2", 200, 300, "COUPON-1", "20.00");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_LOYALTY_POINTS_REDEEMED), eq("user-2"),
                any(SpecificRecord.class));
    }

    @Test
    void publishGiftCardPurchased_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_GIFT_CARD_PURCHASED, "gc-1");

        adapter.publishGiftCardPurchased("gc-1", "CODE-1", "buyer-1", "Buyer Name", "buyer@test.com", "Recipient",
                "rec@test.com", "100.00", "USD", "Happy birthday", "2026-12-31", "design-1");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_GIFT_CARD_PURCHASED), eq("gc-1"),
                any(SpecificRecord.class));
    }

    @Test
    void publishGiftCardRedeemed_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_GIFT_CARD_REDEEMED, "gc-2");

        adapter.publishGiftCardRedeemed("gc-2", "CODE-2", "user-3", "50.00", "50.00", "order-9");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_GIFT_CARD_REDEEMED), eq("gc-2"),
                any(SpecificRecord.class));
    }

    @Test
    void publishNewsletterSubscribed_withSource_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_SUBSCRIBED, "user@test.com");

        adapter.publishNewsletterSubscribed("user@test.com", "user-4", "footer");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_SUBSCRIBED), eq("user@test.com"),
                any(SpecificRecord.class));
    }

    @Test
    void publishNewsletterSubscribed_withNullSource_defaultsToWebsite() {
        stubFuture(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_SUBSCRIBED, "user@test.com");

        adapter.publishNewsletterSubscribed("user@test.com", "user-5", null);

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_SUBSCRIBED), eq("user@test.com"),
                any(SpecificRecord.class));
    }

    @Test
    void publishNewsletterUnsubscribed_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_UNSUBSCRIBED, "bye@test.com");

        adapter.publishNewsletterUnsubscribed("bye@test.com", "user-6");

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_CUSTOMER_NEWSLETTER_UNSUBSCRIBED), eq("bye@test.com"),
                any(SpecificRecord.class));
    }

    @Test
    void publishCurrencyRatesSynced_sendsToCorrectTopic() {
        stubFuture(AppConstants.KAFKA_TOPIC_CONFIG_CURRENCY_RATE_UPDATED, "currency-sync");

        adapter.publishCurrencyRatesSynced(7);

        verify(kafkaTemplate).send(eq(AppConstants.KAFKA_TOPIC_CONFIG_CURRENCY_RATE_UPDATED), eq("currency-sync"),
                any(SpecificRecord.class));
    }
}
