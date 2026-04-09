package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRateJpaRepository extends JpaRepository<CurrencyRateEntity, String> {
    List<CurrencyRateEntity> findByActive(boolean active);

    Optional<CurrencyRateEntity> findByCurrencyCode(String currencyCode);

    boolean existsByCurrencyCode(String currencyCode);
}
