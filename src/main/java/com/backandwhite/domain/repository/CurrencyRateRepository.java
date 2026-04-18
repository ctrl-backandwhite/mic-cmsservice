package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.CurrencyRate;
import java.util.List;
import java.util.Optional;

public interface CurrencyRateRepository {
    List<CurrencyRate> findAll();

    List<CurrencyRate> findByActive(boolean active);

    Optional<CurrencyRate> findByCurrencyCode(String code);

    CurrencyRate save(CurrencyRate rate);

    List<CurrencyRate> saveAll(List<CurrencyRate> rates);
}
