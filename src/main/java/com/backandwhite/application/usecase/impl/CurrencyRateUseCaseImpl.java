package com.backandwhite.application.usecase.impl;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

import com.backandwhite.application.port.out.CurrencyLayerPort;
import com.backandwhite.application.usecase.CurrencyRateUseCase;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.domain.repository.CurrencyRateRepository;
import com.backandwhite.infrastructure.external.CurrencyMetadataProvider;
import com.backandwhite.infrastructure.external.CurrencyMetadataProvider.CurrencyMeta;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CurrencyRateUseCaseImpl implements CurrencyRateUseCase {

    private final CurrencyRateRepository repository;
    private final CurrencyLayerPort client;
    private final com.backandwhite.application.port.out.CmsEventPort eventPort;

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyRate> findAll(Boolean activeOnly) {
        if (Boolean.TRUE.equals(activeOnly)) {
            return repository.findByActive(true);
        }
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyRate findByCode(String code) {
        return repository.findByCurrencyCode(code.toUpperCase())
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("CurrencyRate", code));
    }

    @Override
    @Transactional
    public CurrencyRate toggleActive(String code, boolean active) {
        CurrencyRate rate = findByCode(code);
        rate.setActive(active);
        return repository.save(rate);
    }

    @Override
    @Transactional
    public int syncFromApi() {
        List<com.backandwhite.domain.valueobject.ExchangeRate> liveRates = client.fetchLatestRates();
        if (liveRates.isEmpty()) {
            log.warn("::> No rates returned from CurrencyLayer API");
            return 0;
        }

        Instant now = Instant.now();
        List<CurrencyRate> toSave = new ArrayList<>();

        for (com.backandwhite.domain.valueobject.ExchangeRate entry : liveRates) {
            String code = entry.currencyCode();
            BigDecimal rateValue = entry.rate();

            CurrencyMeta meta = CurrencyMetadataProvider.get(code);
            if (meta == null) {
                meta = CurrencyMetadataProvider.defaultMeta(code);
            }

            Optional<CurrencyRate> existing = repository.findByCurrencyCode(code);
            CurrencyRate rate;

            if (existing.isPresent()) {
                rate = existing.get();
                rate.setRate(rateValue);
                rate.setCurrencyName(meta.currencyName());
                rate.setCurrencySymbol(meta.currencySymbol());
                rate.setCountryName(meta.countryName());
                rate.setCountryCode(meta.countryCode());
                rate.setFlagEmoji(meta.flagEmoji());
                rate.setTimezone(meta.timezone());
                rate.setLanguage(meta.language());
                rate.setLastSyncedAt(now);
            } else {
                rate = CurrencyRate.builder().id(UUID.randomUUID().toString()).currencyCode(code)
                        .currencyName(meta.currencyName()).currencySymbol(meta.currencySymbol())
                        .countryName(meta.countryName()).countryCode(meta.countryCode()).flagEmoji(meta.flagEmoji())
                        .timezone(meta.timezone()).language(meta.language()).rate(rateValue).active(false)
                        .lastSyncedAt(now).build();
            }

            toSave.add(rate);
        }

        // Ensure USD always exists with rate 1.0
        boolean hasUsd = liveRates.stream().anyMatch(r -> "USD".equalsIgnoreCase(r.currencyCode()));
        if (!hasUsd) {
            repository.findByCurrencyCode("USD").ifPresent(usd -> {
                usd.setRate(BigDecimal.ONE);
                usd.setLastSyncedAt(now);
                toSave.add(usd);
            });
        }

        repository.saveAll(toSave);
        log.info("::> Synced {} currency rates from CurrencyLayer", toSave.size());
        eventPort.publishCurrencyRatesSynced(toSave.size());
        return toSave.size();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal convert(BigDecimal amount, String fromCode, String toCode) {
        if (fromCode.equalsIgnoreCase(toCode)) {
            return amount;
        }

        CurrencyRate from = findByCode(fromCode);
        CurrencyRate to = findByCode(toCode);

        // Convert: amount in FROM → USD → TO using Money.convertViaUsd
        return Money.of(amount).convertViaUsd(from.getRate(), to.getRate()).getAmount();
    }
}
