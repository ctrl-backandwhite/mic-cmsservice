package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.repository.SettingRepository;
import com.backandwhite.domain.valueobject.SettingSection;
import com.backandwhite.infrastructure.db.postgres.mapper.SettingInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SettingJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettingRepositoryImpl implements SettingRepository {

    private final SettingJpaRepository jpa;
    private final SettingInfraMapper mapper;

    @Override
    public Setting save(Setting setting) {
        return mapper.toDomain(jpa.save(mapper.toEntity(setting)));
    }

    @Override
    public Setting update(Setting setting) {
        return mapper.toDomain(jpa.save(mapper.toEntity(setting)));
    }

    @Override
    public Optional<Setting> findByKey(String key) {
        return jpa.findById(key).map(mapper::toDomain);
    }

    @Override
    public List<Setting> findBySection(SettingSection section) {
        return jpa.findBySection(section).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Setting> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(String key) {
        jpa.deleteById(key);
    }
}
