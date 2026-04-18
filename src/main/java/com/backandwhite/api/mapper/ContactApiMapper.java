package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.ContactMessageDtoIn;
import com.backandwhite.api.dto.out.ContactMessageDtoOut;
import com.backandwhite.domain.model.ContactMessage;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactApiMapper {
    ContactMessageDtoOut toDto(ContactMessage message);

    List<ContactMessageDtoOut> toDtoList(List<ContactMessage> messages);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "read", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ContactMessage toDomain(ContactMessageDtoIn dto);
}
