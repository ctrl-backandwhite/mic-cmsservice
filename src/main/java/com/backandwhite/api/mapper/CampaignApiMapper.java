package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.CampaignDtoIn;
import com.backandwhite.api.dto.out.CampaignDtoOut;
import com.backandwhite.domain.model.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CampaignApiMapper {
    CampaignDtoOut toDto(Campaign campaign);

    List<CampaignDtoOut> toDtoList(List<Campaign> campaigns);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Campaign toDomain(CampaignDtoIn dto);
}
