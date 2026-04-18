package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.domain.repository.CurrencyRateRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.CurrencyRateInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.CurrencyRateJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRateRepositoryImpl implements CurrencyRateRepository {

    private final CurrencyRateJpaRepository jpa;
    private final CurrencyRateInfraMapper mapper;

    @Override
    public List<CurrencyRate> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<CurrencyRate> findByActive(boolean active) {
        return jpa.findByActive(active).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<CurrencyRate> findByCurrencyCode(String code) {
        return jpa.findByCurrencyCode(code).map(mapper::toDomain);
    }

    @Override
    public CurrencyRate save(CurrencyRate rate) {
        return mapper.toDomain(jpa.save(mapper.toEntity(rate)));
    }

    @Override
    public List<CurrencyRate> saveAll(List<CurrencyRate> rates) {
        var entities = rates.stream().map(mapper::toEntity).toList();
        return jpa.saveAll(entities).stream().map(mapper::toDomain).toList();
    }
}
