package com.backandwhite.infrastructure.external;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP client for the CurrencyLayer API.
 * Fetches live exchange rates relative to USD.
 */
@Log4j2
@Component
public class CurrencyLayerClient {

    private final RestClient restClient;
    private final String apiUrl;
    private final String accessKey;

    public CurrencyLayerClient(
            @Value("${currency-layer.api-url}") String apiUrl,
            @Value("${currency-layer.access-key}") String accessKey) {
        this.restClient = RestClient.builder().build();
        this.apiUrl = apiUrl;
        this.accessKey = accessKey;
    }

    /**
     * Fetches live rates from CurrencyLayer API.
     * 
     * @return Map of currency code (e.g., "EUR") → rate (e.g., 0.925).
     *         Empty map if the call fails or API responds with success=false.
     */
    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> fetchLiveRates() {
        try {
            String url = apiUrl + "?access_key=" + accessKey;
            log.info("::> Fetching live rates from CurrencyLayer API");

            Map<String, Object> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                log.error("::> CurrencyLayer API returned null response");
                return Collections.emptyMap();
            }

            Boolean success = (Boolean) response.get("success");
            if (!Boolean.TRUE.equals(success)) {
                Map<String, Object> error = (Map<String, Object>) response.get("error");
                log.error("::> CurrencyLayer API error: {}", error);
                return Collections.emptyMap();
            }

            Map<String, Object> quotes = (Map<String, Object>) response.get("quotes");
            if (quotes == null || quotes.isEmpty()) {
                log.warn("::> CurrencyLayer API returned empty quotes");
                return Collections.emptyMap();
            }

            Map<String, BigDecimal> rates = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : quotes.entrySet()) {
                // Keys are like "USDEUR", "USDGBP", etc. — strip "USD" prefix
                String key = entry.getKey();
                if (key.length() == 6 && key.startsWith("USD")) {
                    String code = key.substring(3);
                    BigDecimal rate = new BigDecimal(entry.getValue().toString());
                    rates.put(code, rate);
                }
            }

            log.info("::> Fetched {} currency rates from CurrencyLayer", rates.size());
            return rates;

        } catch (Exception e) {
            log.error("::> Failed to fetch rates from CurrencyLayer: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
}
