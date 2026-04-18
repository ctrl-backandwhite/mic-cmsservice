package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.CurrencyRateEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRateJpaRepository extends JpaRepository<CurrencyRateEntity, String> {
    List<CurrencyRateEntity> findByActive(boolean active);

    Optional<CurrencyRateEntity> findByCurrencyCode(String currencyCode);

    boolean existsByCurrencyCode(String currencyCode);
}
