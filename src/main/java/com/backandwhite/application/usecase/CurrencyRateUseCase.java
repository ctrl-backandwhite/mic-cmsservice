package com.backandwhite.application.usecase;

import com.backandwhite.domain.model.CurrencyRate;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyRateUseCase {
    List<CurrencyRate> findAll(Boolean activeOnly);

    CurrencyRate findByCode(String code);

    CurrencyRate toggleActive(String code, boolean active);

    int syncFromApi();

    BigDecimal convert(BigDecimal amount, String fromCode, String toCode);
}
