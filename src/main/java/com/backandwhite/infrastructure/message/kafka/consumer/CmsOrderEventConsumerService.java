package com.backandwhite.infrastructure.message.kafka.consumer;

import com.backandwhite.application.service.LoyaltyMachine;
import com.backandwhite.application.service.LoyaltyResult;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.core.kafka.avro.OrderCancelledEvent;
import com.backandwhite.core.kafka.avro.OrderConfirmedEvent;
import com.backandwhite.core.kafka.avro.OrderDeliveredEvent;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumes order lifecycle events to drive the loyalty program.
 *
 * <p>
 * Policy: points are credited when the order is <b>CONFIRMED</b> (payment
 * succeeded) so the customer sees the reward immediately. If the order is later
 * cancelled we compensate by subtracting the same amount that was awarded —
 * keeping the balance consistent.
 *
 * <p>
 * We still listen to {@code order.delivered} as a safety net in case the
 * pipeline ever reaches that state without having emitted
 * {@code order.confirmed} (e.g. historical orders replayed into the topic).
 * {@link LoyaltyMachine} itself is idempotent per {@code orderId}, so duplicate
 * signals are harmless.
 */
@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class CmsOrderEventConsumerService {

    private final LoyaltyMachine loyaltyMachine;

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_CONFIRMED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        String totalUsd = str(event.getTotalAmountUsd());
        String totalLocal = str(event.getTotalAmount());
        String currency = str(event.getCurrency());
        log.info("::> Received order.confirmed in CMS: orderId={}, userId={}, totalUsd={}, totalLocal={} {}", orderId,
                userId, totalUsd, totalLocal, currency);
        awardPoints(userId, orderId, totalUsd != null ? totalUsd : totalLocal);
    }

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_DELIVERED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderDelivered(OrderDeliveredEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        String totalAmount = str(event.getTotalAmount());
        log.info("::> Received order.delivered in CMS: orderId={}, userId={}, total={}", orderId, userId, totalAmount);
        awardPoints(userId, orderId, totalAmount);
    }

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_CANCELLED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderCancelled(OrderCancelledEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        log.info("::> Received order.cancelled in CMS: orderId={}, userId={}", orderId, userId);
        try {
            loyaltyMachine.reverseOrderPoints(userId, orderId);
        } catch (RuntimeException e) {
            log.error("::> Failed to reverse loyalty points for cancelled order={}: {}", orderId, e.getMessage(), e);
        }
    }

    private void awardPoints(String userId, String orderId, String amount) {
        try {
            BigDecimal amountUsd = parseAmount(amount);
            if (amountUsd.compareTo(BigDecimal.ZERO) <= 0)
                return;
            LoyaltyResult result = loyaltyMachine.processOrderDelivery(userId, amountUsd, orderId);
            log.info("::> Loyalty processed: user={}, earned={}, bonus={}, tier={}, levelUp={}", userId,
                    result.getEarnedPoints(), result.getBonusPoints(), result.getCurrentTier(), result.isLeveledUp());
        } catch (RuntimeException e) {
            log.error("::> Failed to award loyalty points for order={}: {}", orderId, e.getMessage(), e);
        }
    }

    private String str(CharSequence cs) {
        return cs != null ? cs.toString() : null;
    }

    private BigDecimal parseAmount(String amount) {
        if (amount == null || amount.isBlank())
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
