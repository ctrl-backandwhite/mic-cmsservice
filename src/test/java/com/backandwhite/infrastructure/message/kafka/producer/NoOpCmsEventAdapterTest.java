package com.backandwhite.infrastructure.message.kafka.producer;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

class NoOpCmsEventAdapterTest {

    private final NoOpCmsEventAdapter adapter = new NoOpCmsEventAdapter();

    @Test
    void publishLoyaltyPointsEarned_doesNotThrow() {
        assertThatCode(() -> adapter.publishLoyaltyPointsEarned("u-1", "o-1", 10, 100, "GOLD"))
                .doesNotThrowAnyException();
    }

    @Test
    void publishLoyaltyPointsRedeemed_doesNotThrow() {
        assertThatCode(() -> adapter.publishLoyaltyPointsRedeemed("u-1", 5, 50, "C-1", "5.00"))
                .doesNotThrowAnyException();
    }

    @Test
    void publishGiftCardPurchased_doesNotThrow() {
        assertThatCode(() -> adapter.publishGiftCardPurchased("gc-1", "CODE", "buyer", "Name", "b@b.com", "Rec",
                "r@r.com", "100", "USD", "msg", "2026-01-01", "d-1")).doesNotThrowAnyException();
    }

    @Test
    void publishGiftCardRedeemed_doesNotThrow() {
        assertThatCode(() -> adapter.publishGiftCardRedeemed("gc-1", "CODE", "u-1", "10", "90", "o-1"))
                .doesNotThrowAnyException();
    }

    @Test
    void publishNewsletterSubscribed_doesNotThrow() {
        assertThatCode(() -> adapter.publishNewsletterSubscribed("a@a.com", "u-1", "footer"))
                .doesNotThrowAnyException();
    }

    @Test
    void publishNewsletterUnsubscribed_doesNotThrow() {
        assertThatCode(() -> adapter.publishNewsletterUnsubscribed("a@a.com", "u-1")).doesNotThrowAnyException();
    }

    @Test
    void publishCurrencyRatesSynced_doesNotThrow() {
        assertThatCode(() -> adapter.publishCurrencyRatesSynced(3)).doesNotThrowAnyException();
    }

    @Test
    void allMethods_haveNoSideEffects() {
        NoOpCmsEventAdapter localAdapter = new NoOpCmsEventAdapter();
        assertThatCode(() -> {
            localAdapter.publishLoyaltyPointsEarned(null, null, 0, 0, null);
            localAdapter.publishLoyaltyPointsRedeemed(null, 0, 0, null, null);
            localAdapter.publishGiftCardPurchased(null, null, null, null, null, null, null, null, null, null, null,
                    null);
            localAdapter.publishGiftCardRedeemed(null, null, null, null, null, null);
            localAdapter.publishNewsletterSubscribed(null, null, null);
            localAdapter.publishNewsletterUnsubscribed(null, null);
            localAdapter.publishCurrencyRatesSynced(0);
        }).doesNotThrowAnyException();
    }
}
