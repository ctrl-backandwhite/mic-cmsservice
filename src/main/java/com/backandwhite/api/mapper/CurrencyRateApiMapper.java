package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.out.CurrencyRateDtoOut;
import com.backandwhite.domain.model.CurrencyRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyRateApiMapper {
    CurrencyRateDtoOut toDto(CurrencyRate domain);

    List<CurrencyRateDtoOut> toDtoList(List<CurrencyRate> domains);
}
