package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.Slide;
import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlideInfraMapper {

    Slide toDomain(SlideEntity entity);

    SlideEntity toEntity(Slide domain);
}
