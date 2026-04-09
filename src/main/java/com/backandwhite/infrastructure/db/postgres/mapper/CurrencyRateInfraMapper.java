package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.infrastructure.db.postgres.entity.CurrencyRateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyRateInfraMapper {
    CurrencyRate toDomain(CurrencyRateEntity entity);

    CurrencyRateEntity toEntity(CurrencyRate domain);
}
