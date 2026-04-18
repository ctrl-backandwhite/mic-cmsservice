package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.domain.valueobject.SettingSection;
import com.backandwhite.infrastructure.db.postgres.entity.SettingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingJpaRepository extends JpaRepository<SettingEntity, String> {

    List<SettingEntity> findBySection(SettingSection section);
}
