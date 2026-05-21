package com.backandwhite.infrastructure.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.valueobject.ExchangeRate;
import com.backandwhite.infrastructure.external.dto.CurrencyLayerErrorDto;
import com.backandwhite.infrastructure.external.dto.CurrencyLayerResponseDto;
import com.backandwhite.infrastructure.external.mapper.CurrencyLayerMapper;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

class CurrencyLayerAdapterTest {

    private static final String API_URL = "http://api.example.com/live";
    private static final String ACCESS_KEY = "test-key";

    private CurrencyLayerAdapter adapter;
    private RestClient restClient;
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersUriSpec uriSpec;
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersSpec headersSpec;
    private RestClient.ResponseSpec responseSpec;
    private CurrencyLayerMapper mapper;

    @BeforeEach
    @SuppressWarnings({"unchecked", "rawtypes"})
    void setUp() throws Exception {
        mapper = mock(CurrencyLayerMapper.class);
        adapter = new CurrencyLayerAdapter(API_URL, ACCESS_KEY, mapper);

        restClient = mock(RestClient.class);
        uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        headersSpec = mock(RestClient.RequestHeadersSpec.class);
        responseSpec = mock(RestClient.ResponseSpec.class);

        Field f = CurrencyLayerAdapter.class.getDeclaredField("restClient");
        f.setAccessible(true);
        f.set(adapter, restClient);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void fetchLatestRates_nullResponse_returnsEmpty() {
        when(responseSpec.body(CurrencyLayerResponseDto.class)).thenReturn(null);

        List<ExchangeRate> rates = adapter.fetchLatestRates();

        assertThat(rates).isEmpty();
    }

    @Test
    void fetchLatestRates_unsuccessfulResponse_returnsEmpty() {
        CurrencyLayerErrorDto error = new CurrencyLayerErrorDto(101, "invalid_access_key", "bad");
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(false, 0L, "USD", null, error);
        when(responseSpec.body(CurrencyLayerResponseDto.class)).thenReturn(dto);

        List<ExchangeRate> rates = adapter.fetchLatestRates();

        assertThat(rates).isEmpty();
    }

    @Test
    void fetchLatestRates_nullSuccessFlag_returnsEmpty() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(null, 0L, "USD", null, null);
        when(responseSpec.body(CurrencyLayerResponseDto.class)).thenReturn(dto);

        List<ExchangeRate> rates = adapter.fetchLatestRates();

        assertThat(rates).isEmpty();
    }

    @Test
    void fetchLatestRates_successResponse_delegatesToMapper() {
        CurrencyLayerResponseDto dto = new CurrencyLayerResponseDto(true, 1L, "USD",
                Map.of("USDEUR", new BigDecimal("0.92")), null);
        when(responseSpec.body(CurrencyLayerResponseDto.class)).thenReturn(dto);
        when(mapper.toExchangeRates(dto)).thenReturn(List.of(new ExchangeRate("EUR", new BigDecimal("0.92"))));

        List<ExchangeRate> rates = adapter.fetchLatestRates();

        assertThat(rates).hasSize(1);
        assertThat(rates.get(0).currencyCode()).isEqualTo("EUR");
    }

    @Test
    void fetchLatestRates_runtimeException_returnsEmpty() {
        when(responseSpec.body(CurrencyLayerResponseDto.class)).thenThrow(new ResourceAccessException("network"));

        List<ExchangeRate> rates = adapter.fetchLatestRates();

        assertThat(rates).isEmpty();
    }
}
