package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.infrastructure.db.postgres.entity.ContactMessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInfraMapper {

    ContactMessage toDomain(ContactMessageEntity entity);

    ContactMessageEntity toEntity(ContactMessage domain);
}
