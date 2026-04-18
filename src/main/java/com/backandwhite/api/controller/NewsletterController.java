package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.NewsletterSubscribeDtoIn;
import com.backandwhite.api.dto.out.NewsletterSubscriberDtoOut;
import com.backandwhite.api.mapper.NewsletterApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.NewsletterUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import com.backandwhite.domain.model.NewsletterSubscriber;
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
@RequestMapping("/api/v1/newsletter")
@Tag(name = "Newsletter", description = "Endpoints para gestión del newsletter")
public class NewsletterController {

    private final NewsletterUseCase newsletterUseCase;
    private final NewsletterApiMapper newsletterApiMapper;

    @NxPublic
    @PostMapping("/subscribe")
    @Operation(summary = "Suscribirse al newsletter (público)")
    public ResponseEntity<NewsletterSubscriberDtoOut> subscribe(@Valid @RequestBody NewsletterSubscribeDtoIn dto,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        NewsletterSubscriber subscriber = newsletterUseCase.subscribe(dto.getEmail(), dto.getSource());
        return ResponseEntity.status(HttpStatus.CREATED).body(newsletterApiMapper.toDto(subscriber));
    }

    @NxPublic
    @PostMapping("/unsubscribe")
    @Operation(summary = "Desuscribirse del newsletter (público)")
    public ResponseEntity<Void> unsubscribe(@RequestParam String email,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        newsletterUseCase.unsubscribe(email);
        return ResponseEntity.noContent().build();
    }

    @NxAdmin
    @GetMapping
    @Operation(summary = "[Admin] Listar suscriptores")
    public ResponseEntity<PaginationDtoOut<NewsletterSubscriberDtoOut>> findAll(
            @RequestParam(required = false) String status, @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        Map<String, Object> filters = new HashMap<>();
        if (status != null)
            filters.put("status", status);
        if (search != null)
            filters.put("search", search);
        PageResult<NewsletterSubscriber> result = newsletterUseCase.findAll(filters, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, newsletterApiMapper::toDto));
    }

    @NxAdmin
    @GetMapping("/{id}")
    @Operation(summary = "[Admin] Obtener suscriptor por ID")
    public ResponseEntity<NewsletterSubscriberDtoOut> findById(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(newsletterApiMapper.toDto(newsletterUseCase.findById(id)));
    }

    @NxAdmin
    @DeleteMapping("/{id}")
    @Operation(summary = "[Admin] Eliminar suscriptor")
    public ResponseEntity<Void> delete(@PathVariable String id,
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        newsletterUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @NxAdmin
    @GetMapping("/count")
    @Operation(summary = "[Admin] Total de suscriptores")
    public ResponseEntity<Long> count(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(newsletterUseCase.count());
    }
}
