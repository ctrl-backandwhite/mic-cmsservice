package com.backandwhite.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.common.exception.EntityNotFoundException;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.repository.SettingRepository;
import com.backandwhite.domain.valueobject.SettingSection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingUseCaseImplTest {

    @Mock
    private SettingRepository repository;

    @InjectMocks
    private SettingUseCaseImpl useCase;

    private Setting setting() {
        return Setting.builder().key("k1").value(Map.of("a", 1)).section(SettingSection.GENERAL).build();
    }

    @Test
    void save_whenExists_updates() {
        Setting s = setting();
        when(repository.findByKey("k1")).thenReturn(Optional.of(s));
        when(repository.update(s)).thenReturn(s);

        assertThat(useCase.save(s)).isSameAs(s);
        verify(repository).update(s);
        verify(repository, never()).save(any());
    }

    @Test
    void save_whenNew_saves() {
        Setting s = setting();
        when(repository.findByKey("k1")).thenReturn(Optional.empty());
        when(repository.save(s)).thenReturn(s);

        assertThat(useCase.save(s)).isSameAs(s);
        verify(repository).save(s);
        verify(repository, never()).update(any());
    }

    @Test
    void findByKey_existing() {
        when(repository.findByKey("k1")).thenReturn(Optional.of(setting()));
        assertThat(useCase.findByKey("k1")).isNotNull();
    }

    @Test
    void findByKey_notFound() {
        when(repository.findByKey("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.findByKey("x")).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findBySection_delegates() {
        List<Setting> list = List.of(setting());
        when(repository.findBySection(SettingSection.GENERAL)).thenReturn(list);
        assertThat(useCase.findBySection(SettingSection.GENERAL)).isSameAs(list);
    }

    @Test
    void findAll_delegates() {
        List<Setting> list = List.of(setting());
        when(repository.findAll()).thenReturn(list);
        assertThat(useCase.findAll()).isSameAs(list);
    }

    @Test
    void delete_existing() {
        when(repository.findByKey("k1")).thenReturn(Optional.of(setting()));
        useCase.delete("k1");
        verify(repository).delete("k1");
    }

    @Test
    void delete_notFound() {
        when(repository.findByKey("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.delete("x")).isInstanceOf(EntityNotFoundException.class);
    }
}
