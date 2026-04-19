package com.backandwhite.infrastructure.message.kafka.consumer;

import com.backandwhite.application.service.LoyaltyMachine;
import com.backandwhite.application.service.LoyaltyResult;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.core.kafka.avro.OrderDeliveredEvent;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumes order delivery events and delegates loyalty processing to the
 * LoyaltyMachine.
 *
 * Points are awarded using formula: points = floor(orderTotal × pointsPerUnit ×
 * tierMultiplier)
 */
@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class CmsOrderEventConsumerService {

    private final LoyaltyMachine loyaltyMachine;

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_DELIVERED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderDelivered(OrderDeliveredEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        String totalAmount = str(event.getTotalAmount());
        log.info("::> Received order.delivered in CMS: orderId={}, userId={}, total={}", orderId, userId, totalAmount);
        try {
            BigDecimal amount = parseAmount(totalAmount);
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                return;

            LoyaltyResult result = loyaltyMachine.processOrderDelivery(userId, amount, orderId);
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
