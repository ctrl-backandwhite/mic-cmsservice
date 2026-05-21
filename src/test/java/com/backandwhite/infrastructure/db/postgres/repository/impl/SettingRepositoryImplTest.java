package com.backandwhite.infrastructure.db.postgres.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;
import com.backandwhite.infrastructure.db.postgres.entity.SettingEntity;
import com.backandwhite.infrastructure.db.postgres.mapper.SettingInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.SettingJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingRepositoryImplTest {

    @Mock
    private SettingJpaRepository jpa;

    @Mock
    private SettingInfraMapper mapper;

    private SettingRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new SettingRepositoryImpl(jpa, mapper);
    }

    @Test
    void save_delegates() {
        Setting in = Setting.builder().key("k").build();
        SettingEntity entity = new SettingEntity();
        SettingEntity saved = new SettingEntity();
        Setting out = Setting.builder().key("k").build();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.save(in)).isSameAs(out);
    }

    @Test
    void update_delegates() {
        Setting in = Setting.builder().key("k").build();
        SettingEntity entity = new SettingEntity();
        SettingEntity saved = new SettingEntity();
        Setting out = Setting.builder().key("k").build();

        when(mapper.toEntity(in)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(out);

        assertThat(repo.update(in)).isSameAs(out);
    }

    @Test
    void findByKey_present() {
        SettingEntity e = new SettingEntity();
        Setting d = Setting.builder().key("k").build();
        when(jpa.findById("k")).thenReturn(Optional.of(e));
        when(mapper.toDomain(e)).thenReturn(d);

        assertThat(repo.findByKey("k")).contains(d);
    }

    @Test
    void findByKey_empty() {
        when(jpa.findById("k")).thenReturn(Optional.empty());
        assertThat(repo.findByKey("k")).isEmpty();
    }

    @Test
    void findBySection_delegates() {
        SettingEntity e = new SettingEntity();
        when(jpa.findBySection(SettingSection.GENERAL)).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(Setting.builder().key("k").build());

        assertThat(repo.findBySection(SettingSection.GENERAL)).hasSize(1);
    }

    @Test
    void findAll_delegates() {
        SettingEntity e = new SettingEntity();
        when(jpa.findAll()).thenReturn(List.of(e));
        when(mapper.toDomain(e)).thenReturn(Setting.builder().key("k").build());

        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    void delete_delegates() {
        repo.delete("k");
        verify(jpa).deleteById("k");
    }
}
