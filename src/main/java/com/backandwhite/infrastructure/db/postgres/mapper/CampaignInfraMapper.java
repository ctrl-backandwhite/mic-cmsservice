package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.Campaign;
import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CampaignInfraMapper {

    Campaign toDomain(CampaignEntity entity);

    CampaignEntity toEntity(Campaign domain);
}
