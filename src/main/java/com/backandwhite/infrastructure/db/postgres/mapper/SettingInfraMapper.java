package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.Setting;
import com.backandwhite.infrastructure.db.postgres.entity.SettingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingInfraMapper {

    Setting toDomain(SettingEntity entity);

    SettingEntity toEntity(Setting domain);
}
