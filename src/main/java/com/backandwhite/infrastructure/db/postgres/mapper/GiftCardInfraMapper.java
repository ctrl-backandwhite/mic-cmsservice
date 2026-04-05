package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardDesignEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GiftCardInfraMapper {

    GiftCardDesign toDesignDomain(GiftCardDesignEntity entity);

    GiftCardDesignEntity toDesignEntity(GiftCardDesign domain);

    GiftCard toDomain(GiftCardEntity entity);

    GiftCardEntity toEntity(GiftCard domain);

    GiftCardTransaction toTransactionDomain(GiftCardTransactionEntity entity);

    GiftCardTransactionEntity toTransactionEntity(GiftCardTransaction domain);
}
