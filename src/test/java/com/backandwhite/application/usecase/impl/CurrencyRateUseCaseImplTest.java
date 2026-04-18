package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.port.out.CmsEventPort;
import com.backandwhite.application.port.out.CurrencyLayerPort;
import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.domain.repository.CurrencyRateRepository;
import com.backandwhite.domain.valueobject.ExchangeRate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyRateUseCaseImplTest {

    @Mock
    private CurrencyRateRepository repository;

    @Mock
    private CurrencyLayerPort client;

    @Mock
    private CmsEventPort eventPort;

    @InjectMocks
    private CurrencyRateUseCaseImpl useCase;

    private CurrencyRate rate(String code, BigDecimal r) {
        return CurrencyRate.builder().id("id-" + code).currencyCode(code).rate(r).active(true).build();
    }

    @Test
    void findAll_activeTrue_returnsActiveOnly() {
        when(repository.findByActive(true)).thenReturn(List.of(rate("USD", BigDecimal.ONE)));
        assertThat(useCase.findAll(true)).hasSize(1);
    }

    @Test
    void findAll_activeNull_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(rate("USD", BigDecimal.ONE)));
        assertThat(useCase.findAll(null)).hasSize(1);
    }

    @Test
    void findAll_activeFalse_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(rate("USD", BigDecimal.ONE)));
        assertThat(useCase.findAll(false)).hasSize(1);
    }

    @Test
    void findByCode_existing() {
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.of(rate("USD", BigDecimal.ONE)));
        assertThat(useCase.findByCode("usd")).isNotNull();
    }

    @Test
    void findByCode_notFound() {
        when(repository.findByCurrencyCode("ZZZ")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findByCode("zzz")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void toggleActive_togglesAndSaves() {
        CurrencyRate r = rate("USD", BigDecimal.ONE);
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.of(r));
        when(repository.save(r)).thenReturn(r);
        CurrencyRate result = useCase.toggleActive("USD", false);
        assertThat(result.isActive()).isFalse();
    }

    @Test
    void syncFromApi_empty_returnsZero() {
        when(client.fetchLatestRates()).thenReturn(List.of());
        assertThat(useCase.syncFromApi()).isEqualTo(0);
    }

    @Test
    void syncFromApi_updatesExisting() {
        CurrencyRate existing = rate("EUR", new BigDecimal("0.8"));
        when(client.fetchLatestRates()).thenReturn(List.of(new ExchangeRate("EUR", new BigDecimal("0.9"))));
        when(repository.findByCurrencyCode("EUR")).thenReturn(Optional.of(existing));
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.empty());

        int count = useCase.syncFromApi();
        assertThat(count).isEqualTo(1);
        assertThat(existing.getRate()).isEqualByComparingTo("0.9");
        verify(repository).saveAll(anyList());
        verify(eventPort).publishCurrencyRatesSynced(1);
    }

    @Test
    void syncFromApi_createsNew_whenMissing() {
        when(client.fetchLatestRates()).thenReturn(List.of(new ExchangeRate("USD", BigDecimal.ONE)));
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.empty());

        int count = useCase.syncFromApi();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void syncFromApi_addsUsdFromDb_ifNotInLiveRates() {
        CurrencyRate usd = rate("USD", BigDecimal.ONE);
        when(client.fetchLatestRates()).thenReturn(List.of(new ExchangeRate("EUR", new BigDecimal("0.9"))));
        when(repository.findByCurrencyCode("EUR")).thenReturn(Optional.empty());
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.of(usd));

        int count = useCase.syncFromApi();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void syncFromApi_unknownCurrency_usesDefaultMeta() {
        when(client.fetchLatestRates()).thenReturn(List.of(new ExchangeRate("ZZZ", new BigDecimal("2"))));
        when(repository.findByCurrencyCode("ZZZ")).thenReturn(Optional.empty());
        when(repository.findByCurrencyCode("USD")).thenReturn(Optional.empty());

        assertThat(useCase.syncFromApi()).isEqualTo(1);
    }

    @Test
    void convert_sameCurrency_returnsSameAmount() {
        assertThat(useCase.convert(new BigDecimal("10"), "USD", "usd")).isEqualByComparingTo("10");
    }

    @Test
    void convert_differentCurrencies_convertsViaUsd() {
        when(repository.findByCurrencyCode("EUR")).thenReturn(Optional.of(rate("EUR", new BigDecimal("0.8"))));
        when(repository.findByCurrencyCode("GBP")).thenReturn(Optional.of(rate("GBP", new BigDecimal("0.75"))));

        BigDecimal result = useCase.convert(new BigDecimal("100"), "EUR", "GBP");
        assertThat(result).isNotNull();
    }
}
