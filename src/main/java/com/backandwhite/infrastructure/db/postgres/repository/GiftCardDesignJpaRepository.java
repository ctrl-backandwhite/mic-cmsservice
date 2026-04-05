package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.GiftCardDesignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftCardDesignJpaRepository extends JpaRepository<GiftCardDesignEntity, String> {

    List<GiftCardDesignEntity> findByActiveTrue();
}
