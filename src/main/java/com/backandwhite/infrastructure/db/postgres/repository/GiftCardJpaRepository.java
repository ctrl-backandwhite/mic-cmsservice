package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftCardJpaRepository
        extends
            JpaRepository<GiftCardEntity, String>,
            JpaSpecificationExecutor<GiftCardEntity> {

    Optional<GiftCardEntity> findByCode(String code);

    Page<GiftCardEntity> findByBuyerId(String buyerId, Pageable pageable);

    Page<GiftCardEntity> findByRecipientEmail(String email, Pageable pageable);

    List<GiftCardEntity> findByStatusInAndExpiryDateBefore(List<GiftCardStatus> statuses, LocalDate date);

    @Query("""
            SELECT g FROM GiftCardEntity g
            WHERE g.emailSent = false
              AND g.status = com.backandwhite.domain.valueobject.GiftCardStatus.PENDING
              AND (g.sendAt IS NULL OR g.sendAt <= :now)
            """)
    List<GiftCardEntity> findPendingSends(@Param("now") Instant now);
}
