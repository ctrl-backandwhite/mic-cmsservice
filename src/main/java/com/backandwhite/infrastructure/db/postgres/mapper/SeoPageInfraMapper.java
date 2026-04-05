package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.infrastructure.db.postgres.entity.SeoPageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeoPageInfraMapper {

    SeoPage toDomain(SeoPageEntity entity);

    SeoPageEntity toEntity(SeoPage domain);
}
