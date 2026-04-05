package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.GiftCardDesignDtoIn;
import com.backandwhite.api.dto.in.GiftCardPurchaseDtoIn;
import com.backandwhite.api.dto.out.GiftCardDesignDtoOut;
import com.backandwhite.api.dto.out.GiftCardDtoOut;
import com.backandwhite.api.dto.out.GiftCardTransactionDtoOut;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GiftCardApiMapper {
    GiftCardDesignDtoOut toDesignDto(GiftCardDesign design);

    List<GiftCardDesignDtoOut> toDesignDtoList(List<GiftCardDesign> designs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GiftCardDesign toDesignDomain(GiftCardDesignDtoIn dto);

    GiftCardDtoOut toDto(GiftCard card);

    List<GiftCardDtoOut> toDtoList(List<GiftCard> cards);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "buyerId", ignore = true)
    @Mapping(target = "activatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "amount", target = "originalAmount")
    GiftCard toPurchaseDomain(GiftCardPurchaseDtoIn dto);

    GiftCardTransactionDtoOut toTransactionDto(GiftCardTransaction transaction);

    List<GiftCardTransactionDtoOut> toTransactionDtoList(List<GiftCardTransaction> transactions);
}
