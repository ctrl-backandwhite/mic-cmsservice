package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.infrastructure.db.postgres.entity.FlowEntity;
import com.backandwhite.infrastructure.db.postgres.entity.FlowStepEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlowInfraMapper {

    Flow toDomain(FlowEntity entity);

    FlowEntity toEntity(Flow domain);

    FlowStep toStepDomain(FlowStepEntity entity);

    FlowStepEntity toStepEntity(FlowStep domain);
}
