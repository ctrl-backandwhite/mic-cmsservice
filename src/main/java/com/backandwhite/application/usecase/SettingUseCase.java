package com.backandwhite.application.usecase;

import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;
import java.util.List;

public interface SettingUseCase {
    Setting save(Setting setting);

    Setting findByKey(String key);

    List<Setting> findBySection(SettingSection section);

    List<Setting> findAll();

    void delete(String key);
}
