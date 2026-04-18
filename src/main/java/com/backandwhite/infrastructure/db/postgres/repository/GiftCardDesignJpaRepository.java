package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.GiftCardDesignEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftCardDesignJpaRepository extends JpaRepository<GiftCardDesignEntity, String> {

    List<GiftCardDesignEntity> findByActiveTrue();
}
