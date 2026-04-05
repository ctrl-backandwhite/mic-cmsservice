package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.EmailTemplateDtoIn;
import com.backandwhite.api.dto.out.EmailTemplateDtoOut;
import com.backandwhite.domain.model.EmailTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailTemplateApiMapper {
    EmailTemplateDtoOut toDto(EmailTemplate template);

    List<EmailTemplateDtoOut> toDtoList(List<EmailTemplate> templates);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmailTemplate toDomain(EmailTemplateDtoIn dto);
}
