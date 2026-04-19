package com.backandwhite.infrastructure.external;

import com.backandwhite.application.port.out.CurrencyLayerPort;
import com.backandwhite.domain.valueobject.ExchangeRate;
import com.backandwhite.infrastructure.external.dto.CurrencyLayerResponseDto;
import com.backandwhite.infrastructure.external.mapper.CurrencyLayerMapper;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP adapter for the CurrencyLayer API. Fetches live exchange rates quoted
 * against USD and delegates DTO → domain translation to
 * {@link CurrencyLayerMapper}.
 */
@Log4j2
@Component
public class CurrencyLayerAdapter implements CurrencyLayerPort {

    private final RestClient restClient;
    private final String apiUrl;
    private final String accessKey;
    private final CurrencyLayerMapper mapper;

    public CurrencyLayerAdapter(@Value("${currency-layer.api-url}") String apiUrl,
            @Value("${currency-layer.access-key}") String accessKey, CurrencyLayerMapper mapper) {
        this.restClient = RestClient.builder().build();
        this.apiUrl = apiUrl;
        this.accessKey = accessKey;
        this.mapper = mapper;
    }

    @Override
    public List<ExchangeRate> fetchLatestRates() {
        try {
            String url = apiUrl + "?access_key=" + accessKey;
            log.info("::> Fetching live rates from CurrencyLayer API");

            CurrencyLayerResponseDto response = restClient.get().uri(url).retrieve()
                    .body(CurrencyLayerResponseDto.class);

            if (response == null) {
                log.error("::> CurrencyLayer API returned null response");
                return Collections.emptyList();
            }

            if (!Boolean.TRUE.equals(response.success())) {
                log.error("::> CurrencyLayer API error: {}", response.error());
                return Collections.emptyList();
            }

            List<ExchangeRate> rates = mapper.toExchangeRates(response);
            log.info("::> Fetched {} currency rates from CurrencyLayer", rates.size());
            return rates;

        } catch (RuntimeException e) {
            log.error("::> Failed to fetch rates from CurrencyLayer: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
