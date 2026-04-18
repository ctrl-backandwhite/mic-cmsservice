package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.SeoPageDtoIn;
import com.backandwhite.api.dto.out.SeoPageDtoOut;
import com.backandwhite.domain.model.SeoPage;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeoPageApiMapper {
    SeoPageDtoOut toDto(SeoPage seoPage);

    List<SeoPageDtoOut> toDtoList(List<SeoPage> seoPages);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SeoPage toDomain(SeoPageDtoIn dto);
}
