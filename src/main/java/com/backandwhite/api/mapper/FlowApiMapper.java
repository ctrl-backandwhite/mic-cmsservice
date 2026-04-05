package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.FlowDtoIn;
import com.backandwhite.api.dto.in.FlowStepDtoIn;
import com.backandwhite.api.dto.out.FlowDtoOut;
import com.backandwhite.api.dto.out.FlowStepDtoOut;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FlowApiMapper {
    FlowDtoOut toDto(Flow flow);

    List<FlowDtoOut> toDtoList(List<Flow> flows);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Flow toDomain(FlowDtoIn dto);

    FlowStepDtoOut toStepDto(FlowStep step);

    List<FlowStepDtoOut> toStepDtoList(List<FlowStep> steps);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flowId", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FlowStep toStepDomain(FlowStepDtoIn dto);

    List<FlowStep> toStepDomainList(List<FlowStepDtoIn> dtos);
}
