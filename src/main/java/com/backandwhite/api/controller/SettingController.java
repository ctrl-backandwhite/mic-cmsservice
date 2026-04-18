package com.backandwhite.api.controller;

import com.backandwhite.api.dto.in.SettingDtoIn;
import com.backandwhite.api.dto.out.SettingDtoOut;
import com.backandwhite.api.mapper.SettingApiMapper;
import com.backandwhite.application.usecase.SettingUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.valueobject.SettingSection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
@Tag(name = "Settings", description = "Endpoints para gestión de configuraciones")
public class SettingController {

    private final SettingUseCase settingUseCase;
    private final SettingApiMapper settingApiMapper;

    @NxAdmin
    @GetMapping
    @Operation(summary = "[Admin] Listar todas las configuraciones")
    public ResponseEntity<List<SettingDtoOut>> findAll(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(settingApiMapper.toDtoList(settingUseCase.findAll()));
    }

    @NxAdmin
    @GetMapping("/section/{section}")
    @Operation(summary = "[Admin] Obtener configuraciones por sección")
    public ResponseEntity<List<SettingDtoOut>> findBySection(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @PathVariable SettingSection section) {
        return ResponseEntity.ok(settingApiMapper.toDtoList(settingUseCase.findBySection(section)));
    }

    @NxAdmin
    @GetMapping("/{key}")
    @Operation(summary = "[Admin] Obtener configuración por clave")
    public ResponseEntity<SettingDtoOut> findByKey(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String key) {
        return ResponseEntity.ok(settingApiMapper.toDto(settingUseCase.findByKey(key)));
    }

    @NxAdmin
    @PutMapping
    @Operation(summary = "[Admin] Crear o actualizar configuración")
    public ResponseEntity<SettingDtoOut> save(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @Valid @RequestBody SettingDtoIn dto) {
        Setting saved = settingUseCase.save(settingApiMapper.toDomain(dto));
        return ResponseEntity.ok(settingApiMapper.toDto(saved));
    }

    @NxAdmin
    @DeleteMapping("/{key}")
    @Operation(summary = "[Admin] Eliminar configuración")
    public ResponseEntity<Void> delete(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String key) {
        settingUseCase.delete(key);
        return ResponseEntity.noContent().build();
    }
}
