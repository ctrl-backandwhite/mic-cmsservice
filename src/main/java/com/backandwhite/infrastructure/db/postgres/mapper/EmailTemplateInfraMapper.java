package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailTemplateInfraMapper {

    EmailTemplate toDomain(EmailTemplateEntity entity);

    EmailTemplateEntity toEntity(EmailTemplate domain);
}
