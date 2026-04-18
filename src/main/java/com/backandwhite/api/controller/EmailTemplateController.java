package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.EmailTemplateDtoIn;
import com.backandwhite.api.dto.out.EmailTemplateDtoOut;
import com.backandwhite.api.mapper.EmailTemplateApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.EmailTemplateUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.domain.model.EmailTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email-templates")
@Tag(name = "EmailTemplates", description = "Endpoints para gestión de plantillas de email")
public class EmailTemplateController {

    private final EmailTemplateUseCase emailTemplateUseCase;
    private final EmailTemplateApiMapper emailTemplateApiMapper;

    @GetMapping
    @NxAdmin
    @Operation(summary = "[Admin] Listar plantillas")
    public ResponseEntity<PaginationDtoOut<EmailTemplateDtoOut>> findAll(
            @RequestParam(required = false) String category, @RequestParam(required = false) String triggerType,
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Map<String, Object> filters = new HashMap<>();
        if (category != null)
            filters.put("category", category);
        if (triggerType != null)
            filters.put("triggerType", triggerType);
        if (search != null)
            filters.put("search", search);
        PageResult<EmailTemplate> result = emailTemplateUseCase.findAll(filters, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, emailTemplateApiMapper::toDto));
    }

    @GetMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener plantilla por ID")
    public ResponseEntity<EmailTemplateDtoOut> findById(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(emailTemplateApiMapper.toDto(emailTemplateUseCase.findById(id)));
    }

    @GetMapping("/name/{name}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener plantilla por nombre")
    public ResponseEntity<EmailTemplateDtoOut> findByName(@PathVariable String name,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(emailTemplateApiMapper.toDto(emailTemplateUseCase.findByName(name)));
    }

    @PostMapping
    @NxAdmin
    @Operation(summary = "[Admin] Crear plantilla")
    public ResponseEntity<EmailTemplateDtoOut> create(@Valid @RequestBody EmailTemplateDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        EmailTemplate created = emailTemplateUseCase.create(emailTemplateApiMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(emailTemplateApiMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Actualizar plantilla")
    public ResponseEntity<EmailTemplateDtoOut> update(@PathVariable String id,
            @Valid @RequestBody EmailTemplateDtoIn dto, @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        EmailTemplate updated = emailTemplateUseCase.update(id, emailTemplateApiMapper.toDomain(dto));
        return ResponseEntity.ok(emailTemplateApiMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Eliminar plantilla")
    public ResponseEntity<Void> delete(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        emailTemplateUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
