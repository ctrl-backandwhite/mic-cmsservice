package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.GiftCardTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftCardTransactionJpaRepository extends JpaRepository<GiftCardTransactionEntity, String> {

    List<GiftCardTransactionEntity> findByGiftCardIdOrderByCreatedAtDesc(String giftCardId);
}
