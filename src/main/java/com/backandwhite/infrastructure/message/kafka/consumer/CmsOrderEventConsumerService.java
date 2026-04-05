package com.backandwhite.infrastructure.message.kafka.consumer;

import com.backandwhite.application.usecase.LoyaltyUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.core.kafka.avro.OrderConfirmedEvent;
import com.backandwhite.core.kafka.avro.OrderDeliveredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumes order events to award loyalty points.
 * Points are awarded on order delivery.
 */
@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class CmsOrderEventConsumerService {

    private final LoyaltyUseCase loyaltyUseCase;

    @KafkaListener(topics = AppConstants.KAFKA_TOPIC_ORDER_DELIVERED, groupId = AppConstants.KAFKA_GROUP_CMS, containerFactory = "avroKafkaListenerContainerFactory")
    public void onOrderDelivered(OrderDeliveredEvent event) {
        String userId = str(event.getUserId());
        String orderId = str(event.getOrderId());
        String totalAmount = str(event.getTotalAmount());
        log.info("::> Received order.delivered in CMS: orderId={}, userId={}, total={}",
                orderId, userId, totalAmount);
        try {
            // Award loyalty points based on order total
            double amount = parseAmount(totalAmount);
            int points = (int) Math.floor(amount);
            if (points > 0) {
                loyaltyUseCase.earnPoints(userId, points, "Order delivery reward", orderId);
                log.info("::> Awarded {} loyalty points to user={} for order={}", points, userId, orderId);
            }
        } catch (Exception e) {
            log.error("::> Failed to award loyalty points for order={}: {}",
                    orderId, e.getMessage(), e);
        }
    }

    private String str(CharSequence cs) {
        return cs != null ? cs.toString() : null;
    }

    private double parseAmount(String amount) {
        if (amount == null || amount.isBlank())
            return 0.0;
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
