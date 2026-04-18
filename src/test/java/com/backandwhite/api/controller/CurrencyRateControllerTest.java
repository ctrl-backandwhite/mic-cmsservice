package com.backandwhite.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.api.dto.in.CurrencyRateToggleDtoIn;
import com.backandwhite.api.dto.out.CurrencyRateDtoOut;
import com.backandwhite.api.mapper.CurrencyRateApiMapper;
import com.backandwhite.application.usecase.CurrencyRateUseCase;
import com.backandwhite.domain.model.CurrencyRate;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyRateControllerTest {
    @Mock
    CurrencyRateUseCase useCase;
    @Mock
    CurrencyRateApiMapper mapper;
    @InjectMocks
    CurrencyRateController controller;

    @Test
    void findAll_ok() {
        when(useCase.findAll(null)).thenReturn(List.of());
        when(mapper.toDtoList(List.of())).thenReturn(List.of());
        assertThat(controller.findAll(null).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void findByCode_ok() {
        CurrencyRate r = CurrencyRate.builder().currencyCode("USD").build();
        when(useCase.findByCode("USD")).thenReturn(r);
        when(mapper.toDto(r)).thenReturn(CurrencyRateDtoOut.builder().currencyCode("USD").build());
        assertThat(controller.findByCode("USD").getBody().getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void toggleActive_ok() {
        CurrencyRate r = CurrencyRate.builder().currencyCode("USD").active(true).build();
        when(useCase.toggleActive("USD", true)).thenReturn(r);
        when(mapper.toDto(r)).thenReturn(CurrencyRateDtoOut.builder().build());
        CurrencyRateToggleDtoIn in = CurrencyRateToggleDtoIn.builder().active(true).build();
        assertThat(controller.toggleActive("tok", "USD", in).getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void sync_ok() {
        when(useCase.syncFromApi()).thenReturn(5);
        assertThat(controller.sync("tok").getBody().getRatesUpdated()).isEqualTo(5);
        verify(useCase).syncFromApi();
    }

    @Test
    void convert_ok() {
        when(useCase.convert(new BigDecimal("100"), "USD", "EUR")).thenReturn(new BigDecimal("90"));
        when(useCase.findByCode("USD")).thenReturn(CurrencyRate.builder().rate(BigDecimal.ONE).build());
        when(useCase.findByCode("EUR")).thenReturn(CurrencyRate.builder().rate(new BigDecimal("0.9")).build());
        assertThat(controller.convert(new BigDecimal("100"), "USD", "EUR").getStatusCode().value()).isEqualTo(200);
    }
}
