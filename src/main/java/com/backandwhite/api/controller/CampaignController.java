package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.CampaignDtoIn;
import com.backandwhite.api.dto.out.CampaignDtoOut;
import com.backandwhite.api.mapper.CampaignApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.CampaignUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import com.backandwhite.domain.model.Campaign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/campaigns")
@Tag(name = "Campaigns", description = "Endpoints para gestion de campanas")
public class CampaignController {

    private final CampaignUseCase campaignUseCase;
    private final CampaignApiMapper campaignApiMapper;

    @GetMapping("/active")
    @NxPublic
    @Operation(summary = "Listar campanas activas (publico)")
    public ResponseEntity<List<CampaignDtoOut>> findAllActive(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "es") String locale) {
        return ResponseEntity.ok(campaignApiMapper.toDtoList(campaignUseCase.findAllActive(locale)));
    }

    @GetMapping
    @NxAdmin
    @Operation(summary = "[Admin] Listar campanas")
    public ResponseEntity<PaginationDtoOut<CampaignDtoOut>> findAll(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestParam(required = false) Boolean active, @RequestParam(required = false) String type,
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {
        Map<String, Object> filters = new HashMap<>();
        if (active != null)
            filters.put("active", active);
        if (type != null)
            filters.put("type", type);
        if (search != null)
            filters.put("search", search);
        PageResult<Campaign> result = campaignUseCase.findAll(filters, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, campaignApiMapper::toDto));
    }

    @GetMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener campana por ID")
    public ResponseEntity<CampaignDtoOut> findById(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        return ResponseEntity.ok(campaignApiMapper.toDto(campaignUseCase.findById(id)));
    }

    @PostMapping
    @NxAdmin
    @Operation(summary = "[Admin] Crear campana")
    public ResponseEntity<CampaignDtoOut> create(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @Valid @RequestBody CampaignDtoIn dto) {
        Campaign created = campaignUseCase.create(campaignApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignApiMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Actualizar campana")
    public ResponseEntity<CampaignDtoOut> update(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id, @Valid @RequestBody CampaignDtoIn dto) {
        Campaign updated = campaignUseCase.update(id, campaignApiMapper.toDomain(dto));
        return ResponseEntity.ok(campaignApiMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Eliminar campana")
    public ResponseEntity<Void> delete(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        campaignUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @NxAdmin
    @Operation(summary = "[Admin] Activar/desactivar campana")
    public ResponseEntity<Void> toggleActive(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        campaignUseCase.toggleActive(id);
        return ResponseEntity.noContent().build();
    }
}
