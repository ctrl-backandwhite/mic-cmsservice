package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.GiftCardTransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftCardTransactionJpaRepository extends JpaRepository<GiftCardTransactionEntity, String> {

    List<GiftCardTransactionEntity> findByGiftCardIdOrderByCreatedAtDesc(String giftCardId);
}
