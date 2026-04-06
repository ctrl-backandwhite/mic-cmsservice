package com.backandwhite.application.job;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.repository.GiftCardRepository;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Daily job that transitions PENDING/ACTIVE gift cards whose expiryDate
 * has passed to EXPIRED status. Runs every day at 02:00.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class GiftCardExpirationJob {

    private final GiftCardRepository giftCardRepository;

    @Scheduled(cron = "0 0 2 * * *") // every day at 02:00
    @Transactional
    public void expireGiftCards() {
        LocalDate today = LocalDate.now();
        List<GiftCard> expired = giftCardRepository.findExpiredCards(today);
        if (expired.isEmpty()) {
            log.debug("No gift cards to expire today");
            return;
        }
        int count = 0;
        for (GiftCard gc : expired) {
            gc.setStatus(GiftCardStatus.EXPIRED);
            giftCardRepository.update(gc);
            count++;
        }
        log.info("Expired {} gift card(s) with expiryDate before {}", count, today);
    }
}
