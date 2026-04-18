package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.ContactMessageDtoIn;
import com.backandwhite.api.dto.out.ContactMessageDtoOut;
import com.backandwhite.api.mapper.ContactApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.ContactUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import com.backandwhite.domain.model.ContactMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
@Tag(name = "Contact", description = "Endpoints para mensajes de contacto")
public class ContactController {

    private final ContactUseCase contactUseCase;
    private final ContactApiMapper contactApiMapper;

    @PostMapping
    @NxPublic
    @Operation(summary = "Enviar mensaje de contacto (público)")
    public ResponseEntity<ContactMessageDtoOut> submit(@Valid @RequestBody ContactMessageDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        ContactMessage message = contactUseCase.submit(contactApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(contactApiMapper.toDto(message));
    }

    @GetMapping
    @NxAdmin
    @Operation(summary = "[Admin] Listar mensajes de contacto")
    public ResponseEntity<PaginationDtoOut<ContactMessageDtoOut>> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        PageResult<ContactMessage> result = contactUseCase.findAll(page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, contactApiMapper::toDto));
    }

    @GetMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener mensaje por ID")
    public ResponseEntity<ContactMessageDtoOut> findById(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(contactApiMapper.toDto(contactUseCase.findById(id)));
    }

    @PatchMapping("/{id}/read")
    @NxAdmin
    @Operation(summary = "[Admin] Marcar mensaje como leído")
    public ResponseEntity<Void> markAsRead(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        contactUseCase.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
