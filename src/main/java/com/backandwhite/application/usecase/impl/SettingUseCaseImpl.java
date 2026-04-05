package com.backandwhite.application.usecase.impl;

import com.backandwhite.application.usecase.SettingUseCase;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.repository.SettingRepository;
import com.backandwhite.domain.valueobject.SettingSection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SettingUseCaseImpl implements SettingUseCase {

    private final SettingRepository settingRepository;

    @Override
    @Transactional
    public Setting save(Setting setting) {
        var existing = settingRepository.findByKey(setting.getKey());
        if (existing.isPresent()) {
            return settingRepository.update(setting);
        }
        return settingRepository.save(setting);
    }

    @Override
    @Transactional(readOnly = true)
    public Setting findByKey(String key) {
        return settingRepository.findByKey(key)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Setting", key));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Setting> findBySection(SettingSection section) {
        return settingRepository.findBySection(section);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(String key) {
        settingRepository.findByKey(key)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("Setting", key));
        settingRepository.delete(key);
    }
}
