package com.backandwhite.api.controller;

import com.backandwhite.api.dto.in.FlowDtoIn;
import com.backandwhite.api.dto.in.FlowStepDtoIn;
import com.backandwhite.api.dto.out.FlowDtoOut;
import com.backandwhite.api.dto.out.FlowStepDtoOut;
import com.backandwhite.api.mapper.FlowApiMapper;
import com.backandwhite.application.usecase.FlowUseCase;
import com.backandwhite.domain.model.Flow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flows")
@Tag(name = "Flows", description = "Endpoints para gestión de flujos operacionales")
public class FlowController {

    private final FlowUseCase flowUseCase;
    private final FlowApiMapper flowApiMapper;

    @GetMapping
    @Operation(summary = "Listar flujos")
    public ResponseEntity<List<FlowDtoOut>> findAll(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(flowApiMapper.toDtoList(flowUseCase.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener flujo por ID (con pasos)")
    public ResponseEntity<FlowDtoOut> findById(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(flowApiMapper.toDto(flowUseCase.findById(id)));
    }

    @PostMapping
    @Operation(summary = "[Admin] Crear flujo")
    public ResponseEntity<FlowDtoOut> create(@Valid @RequestBody FlowDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Flow created = flowUseCase.create(flowApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(flowApiMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "[Admin] Actualizar flujo")
    public ResponseEntity<FlowDtoOut> update(@PathVariable String id, @Valid @RequestBody FlowDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Flow updated = flowUseCase.update(id, flowApiMapper.toDomain(dto));
        return ResponseEntity.ok(flowApiMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[Admin] Eliminar flujo")
    public ResponseEntity<Void> delete(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        flowUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/steps")
    @Operation(summary = "[Admin] Sincronizar pasos del flujo")
    public ResponseEntity<List<FlowStepDtoOut>> syncSteps(
            @PathVariable String id,
            @Valid @RequestBody List<FlowStepDtoIn> dtos,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        var steps = flowUseCase.syncSteps(id, flowApiMapper.toStepDomainList(dtos));
        return ResponseEntity.ok(flowApiMapper.toStepDtoList(steps));
    }

    @GetMapping("/{id}/steps")
    @Operation(summary = "Obtener pasos del flujo")
    public ResponseEntity<List<FlowStepDtoOut>> findSteps(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(flowApiMapper.toStepDtoList(flowUseCase.findStepsByFlowId(id)));
    }
}
