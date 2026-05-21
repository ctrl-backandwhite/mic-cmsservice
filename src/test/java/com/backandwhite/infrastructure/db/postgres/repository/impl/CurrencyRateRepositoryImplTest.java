package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.infrastructure.db.postgres.entity.CurrencyRateEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.CurrencyRateInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.CurrencyRateJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyRateRepositoryImplTest {

    @Mock
    private CurrencyRateJpaRepository jpa;

    @Mock
    private CurrencyRateInfraMapper mapper;

    private CurrencyRateRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new CurrencyRateRepositoryImpl(jpa, mapper);
    }

    private CurrencyRate cr(String code) {
        return CurrencyRate.builder().currencyCode(code).build();
    }

    @Test
    void findAll_mapsToDomain() {
        CurrencyRateEntity e = new CurrencyRateEntity();
        when(jpa.findAll()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(cr("USD"));

        List<CurrencyRate> result = repo.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void findByActive_true() {
        CurrencyRateEntity e = new CurrencyRateEntity();
        when(jpa.findByActive(true)).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(cr("EUR"));

        assertThat(repo.findByActive(true)).extracting(CurrencyRate::getCurrencyCode).containsExactly("EUR");
    }

    @Test
    void findByActive_false_empty() {
        when(jpa.findByActive(false)).thenReturn(List.of());
        assertThat(repo.findByActive(false)).isEmpty();
    }

    @Test
    void findByCurrencyCode_present() {
        CurrencyRateEntity e = new CurrencyRateEntity();
        CurrencyRate d = cr("GBP");
        when(jpa.findByCurrencyCode("GBP")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(d);

        assertThat(repo.findByCurrencyCode("GBP")).contains(d);
    }

    @Test
    void findByCurrencyCode_empty() {
        when(jpa.findByCurrencyCode("XYZ")).thenReturn(Optional.empty());
        assertThat(repo.findByCurrencyCode("XYZ")).isEmpty();
    }

    @Test
    void save_delegates() {
        CurrencyRate in = cr("USD");
        CurrencyRateEntity entity = new CurrencyRateEntity();
        CurrencyRateEntity saved = new CurrencyRateEntity();
        CurrencyRate out = cr("USD");

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.save(in)).isSameAs(out);
        verify(jpa).save(entity);
    }

    @Test
    void saveAll_delegates() {
        CurrencyRate r1 = cr("USD");
        CurrencyRate r2 = cr("EUR");
        CurrencyRateEntity e1 = new CurrencyRateEntity();
        CurrencyRateEntity e2 = new CurrencyRateEntity();
        when(mapper.toEntity(r1)).thenReturn(e1);
        when(mapper.toEntity(r2)).thenReturn(e2);
        when(jpa.saveAll(List.of(e1, e2))).thenReturn(List.of(e1, e2));
        when(mapper.toDomain(e1)).thenReturn(r1);
        when(mapper.toDomain(e2)).thenReturn(r2);

        List<CurrencyRate> result = repo.saveAll(List.of(r1, r2));
        assertThat(result).hasSize(2);
    }
}
