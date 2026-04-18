package com.backandwhite.application.job;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.application.usecase.CurrencyRateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyRateSyncJobTest {

    @Mock
    private CurrencyRateUseCase useCase;

    @InjectMocks
    private CurrencyRateSyncJob job;

    @Test
    void syncCurrencyRates_success_invokesUseCase() {
        when(useCase.syncFromApi()).thenReturn(5);
        job.syncCurrencyRates();
        verify(useCase).syncFromApi();
    }

    @Test
    void syncCurrencyRates_exception_swallowed() {
        when(useCase.syncFromApi()).thenThrow(new RuntimeException("boom"));
        job.syncCurrencyRates();
        verify(useCase).syncFromApi();
    }
}
