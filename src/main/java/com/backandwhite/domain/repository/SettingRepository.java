package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;

import java.util.List;
import java.util.Optional;

public interface SettingRepository {
    Setting save(Setting setting);

    Setting update(Setting setting);

    Optional<Setting> findByKey(String key);

    List<Setting> findBySection(SettingSection section);

    List<Setting> findAll();

    void delete(String key);
}
