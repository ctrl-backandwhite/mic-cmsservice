package com.backandwhite.api.mapper;

import com.backandwhite.api.dto.in.LoyaltyRuleDtoIn;
import com.backandwhite.api.dto.in.LoyaltyTierDtoIn;
import com.backandwhite.api.dto.out.LoyaltyRuleDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTierDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTransactionDtoOut;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LoyaltyApiMapper {
    LoyaltyTierDtoOut toTierDto(LoyaltyTier tier);

    List<LoyaltyTierDtoOut> toTierDtoList(List<LoyaltyTier> tiers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoyaltyTier toTierDomain(LoyaltyTierDtoIn dto);

    LoyaltyRuleDtoOut toRuleDto(LoyaltyRule rule);

    List<LoyaltyRuleDtoOut> toRuleDtoList(List<LoyaltyRule> rules);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoyaltyRule toRuleDomain(LoyaltyRuleDtoIn dto);

    LoyaltyTransactionDtoOut toTransactionDto(LoyaltyTransaction transaction);

    List<LoyaltyTransactionDtoOut> toTransactionDtoList(List<LoyaltyTransaction> transactions);
}
