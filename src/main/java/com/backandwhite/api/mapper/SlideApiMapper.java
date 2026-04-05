package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.SlideDtoIn;
import com.backandwhite.api.dto.out.SlideDtoOut;
import com.backandwhite.domain.model.Slide;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SlideApiMapper {
    SlideDtoOut toDto(Slide slide);

    List<SlideDtoOut> toDtoList(List<Slide> slides);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Slide toDomain(SlideDtoIn dto);
}
