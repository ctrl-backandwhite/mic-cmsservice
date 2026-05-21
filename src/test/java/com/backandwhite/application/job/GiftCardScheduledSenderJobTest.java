package com.backandwhite.application.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.repository.GiftCardRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftCardScheduledSenderJobTest {

    @Mock
    private GiftCardRepository giftCardRepository;

    @Mock
    private CmsEventPort cmsEventPort;

    @InjectMocks
    private GiftCardScheduledSenderJob job;

    private GiftCard sample(String id, LocalDate expiry) {
        return GiftCard.builder().id(id).code("CODE-" + id).buyerId("buyer-" + id).buyerEmail("buyer@x.io")
                .recipientName("Alice").recipientEmail("alice@x.io").message("hi")
                .originalAmount(Money.of(new BigDecimal("50.00"))).expiryDate(expiry).designId("design-1").build();
    }

    @Test
    void dispatchDueGiftCards_noDue_returnsEarly() {
        when(giftCardRepository.findPendingSends(any(Instant.class))).thenReturn(List.of());

        job.dispatchDueGiftCards();

        verify(cmsEventPort, never()).publishGiftCardPurchased(anyString(), anyString(), anyString(), any(),
                anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(), anyString());
        verify(giftCardRepository, never()).update(any());
    }

    @Test
    void dispatchDueGiftCards_publishesAndMarksSent() {
        GiftCard a = sample("a", LocalDate.of(2030, 1, 1));
        GiftCard b = sample("b", null); // covers expiryDate null branch
        when(giftCardRepository.findPendingSends(any(Instant.class))).thenReturn(List.of(a, b));

        job.dispatchDueGiftCards();

        verify(cmsEventPort).publishGiftCardPurchased("a", "CODE-a", "buyer-a", null, "buyer@x.io", "Alice",
                "alice@x.io", "50.00", "USD", "hi", "2030-01-01", "design-1");
        verify(cmsEventPort).publishGiftCardPurchased("b", "CODE-b", "buyer-b", null, "buyer@x.io", "Alice",
                "alice@x.io", "50.00", "USD", "hi", null, "design-1");
        verify(giftCardRepository, times(2)).update(any(GiftCard.class));
    }

    @Test
    void dispatchDueGiftCards_publishFails_swallowsExceptionAndContinues() {
        GiftCard a = sample("a", LocalDate.of(2030, 1, 1));
        GiftCard b = sample("b", LocalDate.of(2030, 1, 1));
        when(giftCardRepository.findPendingSends(any(Instant.class))).thenReturn(List.of(a, b));
        doThrow(new RuntimeException("kafka down")).when(cmsEventPort).publishGiftCardPurchased(eq("a"), anyString(),
                anyString(), any(), anyString(), anyString(), anyString(), anyString(), anyString(), any(), any(),
                anyString());

        job.dispatchDueGiftCards();

        // a fails => no update; b succeeds => update once
        verify(giftCardRepository, times(1)).update(any(GiftCard.class));
    }
}
