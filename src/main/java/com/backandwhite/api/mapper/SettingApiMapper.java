package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.SettingDtoIn;
import com.backandwhite.api.dto.out.SettingDtoOut;
import com.backandwhite.domain.model.Setting;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettingApiMapper {
    SettingDtoOut toDto(Setting setting);

    List<SettingDtoOut> toDtoList(List<Setting> settings);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Setting toDomain(SettingDtoIn dto);
}
