package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.SeoPageDtoIn;
import com.backandwhite.api.dto.out.SeoPageDtoOut;
import com.backandwhite.api.mapper.SeoPageApiMapper;
import com.backandwhite.api.util.PaginationMapper;
import com.backandwhite.application.usecase.SeoPageUseCase;
import com.backandwhite.domain.model.SeoPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seo")
@Tag(name = "SEO Pages", description = "Endpoints para gestión de páginas SEO")
public class SeoPageController {

    private final SeoPageUseCase seoPageUseCase;
    private final SeoPageApiMapper seoPageApiMapper;

    @GetMapping
    @Operation(summary = "[Admin] Listar páginas SEO")
    public ResponseEntity<PaginationDtoOut<SeoPageDtoOut>> findAll(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "path") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        var result = seoPageUseCase.findAll(page, size, sortBy, ascending);
        return ResponseEntity.ok(PaginationMapper.map(result, seoPageApiMapper::toDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "[Admin] Obtener página SEO por ID")
    public ResponseEntity<SeoPageDtoOut> findById(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        return ResponseEntity.ok(seoPageApiMapper.toDto(seoPageUseCase.findById(id)));
    }

    @GetMapping("/path")
    @Operation(summary = "Obtener SEO por path (público)")
    public ResponseEntity<SeoPageDtoOut> findByPath(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestParam String path) {
        return ResponseEntity.ok(seoPageApiMapper.toDto(seoPageUseCase.findByPath(path)));
    }

    @PostMapping
    @Operation(summary = "[Admin] Crear página SEO")
    public ResponseEntity<SeoPageDtoOut> create(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @Valid @RequestBody SeoPageDtoIn dto) {
        SeoPage created = seoPageUseCase.create(seoPageApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(seoPageApiMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "[Admin] Actualizar página SEO")
    public ResponseEntity<SeoPageDtoOut> update(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id, @Valid @RequestBody SeoPageDtoIn dto) {
        SeoPage updated = seoPageUseCase.update(id, seoPageApiMapper.toDomain(dto));
        return ResponseEntity.ok(seoPageApiMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[Admin] Eliminar página SEO")
    public ResponseEntity<Void> delete(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        seoPageUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
