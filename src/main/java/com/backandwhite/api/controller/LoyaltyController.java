package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.LoyaltyEarnDtoIn;
import com.backandwhite.api.dto.in.LoyaltyRuleDtoIn;
import com.backandwhite.api.dto.in.LoyaltyTierDtoIn;
import com.backandwhite.api.dto.out.LoyaltyBalanceDtoOut;
import com.backandwhite.api.dto.out.LoyaltyRuleDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTierDtoOut;
import com.backandwhite.api.dto.out.LoyaltyTransactionDtoOut;
import com.backandwhite.api.mapper.LoyaltyApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.LoyaltyUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import com.backandwhite.common.security.annotation.NxUser;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.common.domain.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loyalty")
@Tag(name = "Loyalty", description = "Endpoints para programa de fidelización")
public class LoyaltyController {

    private final LoyaltyUseCase loyaltyUseCase;
    private final LoyaltyApiMapper loyaltyApiMapper;

    // ── Tiers ────────────────────────────────────────────────────────────

    @GetMapping("/tiers")
    @Operation(summary = "Listar niveles de fidelización")
    public ResponseEntity<List<LoyaltyTierDtoOut>> findAllTiers(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(loyaltyApiMapper.toTierDtoList(loyaltyUseCase.findAllTiers()));
    }

    @GetMapping("/tiers/{id}")
    @Operation(summary = "[Admin] Obtener tier por ID")
    public ResponseEntity<LoyaltyTierDtoOut> findTierById(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        return ResponseEntity.ok(loyaltyApiMapper.toTierDto(loyaltyUseCase.findTierById(id)));
    }

    @PostMapping("/tiers")
    @Operation(summary = "[Admin] Crear tier")
    public ResponseEntity<LoyaltyTierDtoOut> createTier(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @Valid @RequestBody LoyaltyTierDtoIn dto) {
        LoyaltyTier created = loyaltyUseCase.createTier(loyaltyApiMapper.toTierDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(loyaltyApiMapper.toTierDto(created));
    }

    @PutMapping("/tiers/{id}")
    @Operation(summary = "[Admin] Actualizar tier")
    public ResponseEntity<LoyaltyTierDtoOut> updateTier(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id,
            @Valid @RequestBody LoyaltyTierDtoIn dto) {
        LoyaltyTier updated = loyaltyUseCase.updateTier(id, loyaltyApiMapper.toTierDomain(dto));
        return ResponseEntity.ok(loyaltyApiMapper.toTierDto(updated));
    }

    @DeleteMapping("/tiers/{id}")
    @Operation(summary = "[Admin] Eliminar tier")
    public ResponseEntity<Void> deleteTier(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        loyaltyUseCase.deleteTier(id);
        return ResponseEntity.noContent().build();
    }

    // ── Rules ────────────────────────────────────────────────────────────

    @GetMapping("/rules")
    @Operation(summary = "[Admin] Listar reglas")
    public ResponseEntity<List<LoyaltyRuleDtoOut>> findAllRules(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(loyaltyApiMapper.toRuleDtoList(loyaltyUseCase.findAllRules()));
    }

    @GetMapping("/rules/{id}")
    @Operation(summary = "[Admin] Obtener regla por ID")
    public ResponseEntity<LoyaltyRuleDtoOut> findRuleById(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        return ResponseEntity.ok(loyaltyApiMapper.toRuleDto(loyaltyUseCase.findRuleById(id)));
    }

    @PostMapping("/rules")
    @Operation(summary = "[Admin] Crear regla")
    public ResponseEntity<LoyaltyRuleDtoOut> createRule(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @Valid @RequestBody LoyaltyRuleDtoIn dto) {
        LoyaltyRule created = loyaltyUseCase.createRule(loyaltyApiMapper.toRuleDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(loyaltyApiMapper.toRuleDto(created));
    }

    @PutMapping("/rules/{id}")
    @Operation(summary = "[Admin] Actualizar regla")
    public ResponseEntity<LoyaltyRuleDtoOut> updateRule(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id,
            @Valid @RequestBody LoyaltyRuleDtoIn dto) {
        LoyaltyRule updated = loyaltyUseCase.updateRule(id, loyaltyApiMapper.toRuleDomain(dto));
        return ResponseEntity.ok(loyaltyApiMapper.toRuleDto(updated));
    }

    @DeleteMapping("/rules/{id}")
    @Operation(summary = "[Admin] Eliminar regla")
    public ResponseEntity<Void> deleteRule(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        loyaltyUseCase.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    // ── User Points ──────────────────────────────────────────────────────

    @GetMapping("/balance")
    @Operation(summary = "Consultar balance de puntos")
    public ResponseEntity<LoyaltyBalanceDtoOut> getBalance(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = "X-Auth-Subject") String userId) {
        int balance = loyaltyUseCase.getBalance(userId);
        return ResponseEntity.ok(LoyaltyBalanceDtoOut.builder().userId(userId).balance(balance).build());
    }

    @PostMapping("/earn")
    @Operation(summary = "Otorgar puntos")
    public ResponseEntity<LoyaltyTransactionDtoOut> earnPoints(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = "X-Auth-Subject") String userId,
            @Valid @RequestBody LoyaltyEarnDtoIn dto) {
        LoyaltyTransaction transaction = loyaltyUseCase.earnPoints(userId, dto.getPoints(), dto.getDescription(),
                dto.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(loyaltyApiMapper.toTransactionDto(transaction));
    }

    @PostMapping("/redeem")
    @Operation(summary = "Canjear puntos")
    public ResponseEntity<LoyaltyTransactionDtoOut> redeemPoints(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = "X-Auth-Subject") String userId,
            @Valid @RequestBody LoyaltyEarnDtoIn dto) {
        LoyaltyTransaction transaction = loyaltyUseCase.redeemPoints(userId, dto.getPoints(), dto.getDescription(),
                dto.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(loyaltyApiMapper.toTransactionDto(transaction));
    }

    @GetMapping("/redemption-rate")
    @Operation(summary = "Obtener tasa de canjeo (puntos por dólar)")
    public ResponseEntity<java.util.Map<String, Object>> getRedemptionRate(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        List<LoyaltyRule> rules = loyaltyUseCase.findAllRules();
        int pointsPerDollar = rules.stream()
                .filter(r -> r.getAction() == com.backandwhite.domain.valueobject.LoyaltyAction.REDEMPTION
                        && r.isActive())
                .findFirst()
                .map(com.backandwhite.domain.model.LoyaltyRule::getPointsPerUnit)
                .orElse(100); // default 100 pts = $1
        return ResponseEntity.ok(java.util.Map.of("pointsPerDollar", pointsPerDollar));
    }

    @GetMapping("/history")
    @Operation(summary = "Historial de transacciones de puntos")
    public ResponseEntity<PaginationDtoOut<LoyaltyTransactionDtoOut>> getHistory(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = "X-Auth-Subject") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {
        PageResult<LoyaltyTransaction> result = loyaltyUseCase.getHistory(userId, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, loyaltyApiMapper::toTransactionDto));
    }
}
