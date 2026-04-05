package com.backandwhite.infrastructure.db.postgres.mapper;

import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTierEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoyaltyInfraMapper {

    LoyaltyTier toTierDomain(LoyaltyTierEntity entity);

    LoyaltyTierEntity toTierEntity(LoyaltyTier domain);

    LoyaltyRule toRuleDomain(LoyaltyRuleEntity entity);

    LoyaltyRuleEntity toRuleEntity(LoyaltyRule domain);

    LoyaltyTransaction toTransactionDomain(LoyaltyTransactionEntity entity);

    LoyaltyTransactionEntity toTransactionEntity(LoyaltyTransaction domain);
}
