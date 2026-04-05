package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsletterInfraMapper {

    NewsletterSubscriber toDomain(NewsletterSubscriberEntity entity);

    NewsletterSubscriberEntity toEntity(NewsletterSubscriber domain);
}
