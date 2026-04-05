package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.out.NewsletterSubscriberDtoOut;
import com.backandwhite.domain.model.NewsletterSubscriber;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsletterApiMapper {
    NewsletterSubscriberDtoOut toDto(NewsletterSubscriber subscriber);

    List<NewsletterSubscriberDtoOut> toDtoList(List<NewsletterSubscriber> subscribers);
}
