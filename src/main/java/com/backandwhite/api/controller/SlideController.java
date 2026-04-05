package com.backandwhite.api.controller;

import com.backandwhite.api.dto.in.SlideDtoIn;
import com.backandwhite.api.dto.out.SlideDtoOut;
import com.backandwhite.api.mapper.SlideApiMapper;
import com.backandwhite.application.usecase.SlideUseCase;
import com.backandwhite.domain.model.Slide;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slides")
@Tag(name = "Slides", description = "Endpoints para gestión del carrusel")
public class SlideController {

    private final SlideUseCase slideUseCase;
    private final SlideApiMapper slideApiMapper;

    @GetMapping("/active")
    @Operation(summary = "Listar slides activos (público)")
    public ResponseEntity<List<SlideDtoOut>> findAllActive(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(slideApiMapper.toDtoList(slideUseCase.findAllActive()));
    }

    @GetMapping
    @Operation(summary = "[Admin] Listar todos los slides")
    public ResponseEntity<List<SlideDtoOut>> findAll(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(slideApiMapper.toDtoList(slideUseCase.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "[Admin] Obtener slide por ID")
    public ResponseEntity<SlideDtoOut> findById(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(slideApiMapper.toDto(slideUseCase.findById(id)));
    }

    @PostMapping
    @Operation(summary = "[Admin] Crear slide")
    public ResponseEntity<SlideDtoOut> create(@Valid @RequestBody SlideDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Slide created = slideUseCase.create(slideApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(slideApiMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "[Admin] Actualizar slide")
    public ResponseEntity<SlideDtoOut> update(@PathVariable String id, @Valid @RequestBody SlideDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Slide updated = slideUseCase.update(id, slideApiMapper.toDomain(dto));
        return ResponseEntity.ok(slideApiMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[Admin] Eliminar slide")
    public ResponseEntity<Void> delete(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        slideUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/positions")
    @Operation(summary = "[Admin] Reordenar slides")
    public ResponseEntity<Void> updatePositions(@RequestBody List<SlideDtoIn> dtos,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        List<Slide> slides = dtos.stream().map(slideApiMapper::toDomain).toList();
        slideUseCase.updatePositions(slides);
        return ResponseEntity.noContent().build();
    }
}
