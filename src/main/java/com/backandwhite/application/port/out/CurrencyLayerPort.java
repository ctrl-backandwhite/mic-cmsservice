package com.backandwhite.application.port.out;

import com.backandwhite.domain.valueobject.ExchangeRate;
import java.util.List;

/**
 * Port for fetching live exchange rates from an external provider (e.g.
 * CurrencyLayer). The contract is defined in domain terms — adapters are
 * responsible for the HTTP/DTO translation.
 */
public interface CurrencyLayerPort {

    /**
     * Fetch the latest exchange rates quoted against USD.
     *
     * @return immutable list of {@link ExchangeRate}; empty list if the upstream
     *         provider is unavailable or returns an error.
     */
    List<ExchangeRate> fetchLatestRates();
}
