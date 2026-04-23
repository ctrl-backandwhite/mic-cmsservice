package com.backandwhite.application.job;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.repository.GiftCardRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fires the deferred {@code GiftCardPurchasedEvent} for every gift card whose
 * {@code send_date} has arrived. Runs every 5 minutes so a card scheduled for
 * "now" lands in the recipient's inbox within that window. Rows already
 * delivered ({@code email_sent = true}) are skipped; flipping the flag inside
 * the same transaction guarantees at-most-once publication even if two
 * instances of the job race.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class GiftCardScheduledSenderJob {

    private final GiftCardRepository giftCardRepository;
    private final CmsEventPort cmsEventPort;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void dispatchDueGiftCards() {
        LocalDate today = LocalDate.now();
        List<GiftCard> due = giftCardRepository.findPendingSends(today);
        if (due.isEmpty()) {
            return;
        }
        int sent = 0;
        for (GiftCard gc : due) {
            try {
                cmsEventPort.publishGiftCardPurchased(gc.getId(), gc.getCode(), gc.getBuyerId(), null,
                        gc.getBuyerEmail(), gc.getRecipientName(), gc.getRecipientEmail(),
                        gc.getOriginalAmount().toPlainString(), "USD", gc.getMessage(),
                        gc.getExpiryDate() != null ? gc.getExpiryDate().toString() : null, gc.getDesignId());
                gc.setEmailSent(true);
                giftCardRepository.update(gc);
                sent++;
            } catch (RuntimeException e) {
                log.warn("::> Scheduled gift card send failed for id={}: {}", gc.getId(), e.getMessage());
            }
        }
        log.info("::> Dispatched {} scheduled gift card(s) out of {} due", sent, due.size());
    }
}
