package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.domain.valueobject.SettingSection;
import com.backandwhite.infrastructure.db.postgres.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingJpaRepository extends JpaRepository<SettingEntity, String> {

    List<SettingEntity> findBySection(SettingSection section);
}
