package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GiftCardJpaRepository
        extends JpaRepository<GiftCardEntity, String>,
        JpaSpecificationExecutor<GiftCardEntity> {

    Optional<GiftCardEntity> findByCode(String code);

    Page<GiftCardEntity> findByBuyerId(String buyerId, Pageable pageable);

    Page<GiftCardEntity> findByRecipientEmail(String email, Pageable pageable);
}
