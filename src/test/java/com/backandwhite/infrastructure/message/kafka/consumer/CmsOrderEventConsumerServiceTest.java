package com.backandwhite.infrastructure.message.kafka.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.service.LoyaltyMachine;
import com.backandwhite.application.service.LoyaltyResult;
import com.backandwhite.core.kafka.avro.OrderCancelledEvent;
import com.backandwhite.core.kafka.avro.OrderConfirmedEvent;
import com.backandwhite.core.kafka.avro.OrderDeliveredEvent;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CmsOrderEventConsumerServiceTest {

    @Mock
    private LoyaltyMachine loyaltyMachine;

    @InjectMocks
    private CmsOrderEventConsumerService consumer;

    @Test
    void onOrderConfirmed_usesTotalAmountUsdWhenPresent() {
        OrderConfirmedEvent event = OrderConfirmedEvent.newBuilder().setOrderId("o1").setUserId("u1")
                .setOrderReference("NX-1").setTotalAmount("100.00").setCurrency("EUR").setTotalAmountUsd("110.00")
                .setItemCount(2).setTimestamp("now").build();
        when(loyaltyMachine.processOrderDelivery(eq("u1"), any(BigDecimal.class), eq("o1")))
                .thenReturn(LoyaltyResult.empty());

        consumer.onOrderConfirmed(event);

        verify(loyaltyMachine).processOrderDelivery("u1", new BigDecimal("110.00"), "o1");
    }

    @Test
    void onOrderConfirmed_fallsBackToTotalAmountWhenUsdMissing() {
        OrderConfirmedEvent event = OrderConfirmedEvent.newBuilder().setOrderId("o1").setUserId("u1")
                .setOrderReference("NX-1").setTotalAmount("50.00").setCurrency("USD").setItemCount(1)
                .setTimestamp("now").build();
        when(loyaltyMachine.processOrderDelivery(eq("u1"), any(BigDecimal.class), eq("o1")))
                .thenReturn(LoyaltyResult.empty());

        consumer.onOrderConfirmed(event);

        verify(loyaltyMachine).processOrderDelivery("u1", new BigDecimal("50.00"), "o1");
    }

    @Test
    void onOrderConfirmed_zeroAmount_skipsAward() {
        OrderConfirmedEvent event = OrderConfirmedEvent.newBuilder().setOrderId("o1").setUserId("u1")
                .setOrderReference("NX-1").setTotalAmount("0").setCurrency("USD").setTotalAmountUsd("0").setItemCount(0)
                .setTimestamp("now").build();

        consumer.onOrderConfirmed(event);

        verify(loyaltyMachine, never()).processOrderDelivery(any(), any(), any());
    }

    @Test
    void onOrderDelivered_stillAwardsForSafetyNet() {
        OrderDeliveredEvent event = OrderDeliveredEvent.newBuilder().setOrderId("o1").setUserId("u1")
                .setOrderReference("NX-1").setTotalAmount("75.00").setTimestamp("now").build();
        when(loyaltyMachine.processOrderDelivery(eq("u1"), any(BigDecimal.class), eq("o1")))
                .thenReturn(LoyaltyResult.empty());

        consumer.onOrderDelivered(event);

        verify(loyaltyMachine).processOrderDelivery("u1", new BigDecimal("75.00"), "o1");
    }

    @Test
    void onOrderCancelled_reversesPoints() {
        OrderCancelledEvent event = OrderCancelledEvent.newBuilder().setOrderId("o1").setUserId("u1")
                .setOrderReference("NX-1").setReason("customer").setTimestamp("now").build();

        consumer.onOrderCancelled(event);

        verify(loyaltyMachine).reverseOrderPoints("u1", "o1");
    }
}
