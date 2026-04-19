package com.backandwhite.application.job;

import com.backandwhite.application.usecase.CurrencyRateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that syncs currency rates from CurrencyLayer API every 6 hours.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class CurrencyRateSyncJob {

    private final CurrencyRateUseCase currencyRateUseCase;

    @Scheduled(cron = "0 0 */6 * * *")
    public void syncCurrencyRates() {
        log.info("::> Starting scheduled currency rate sync");
        try {
            int count = currencyRateUseCase.syncFromApi();
            log.info("::> Scheduled sync completed: {} rates updated", count);
        } catch (RuntimeException e) {
            log.error("::> Scheduled currency rate sync failed: {}", e.getMessage(), e);
        }
    }
}
