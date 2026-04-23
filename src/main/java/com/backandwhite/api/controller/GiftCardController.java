package com.backandwhite.api.controller;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.api.dto.in.GiftCardDesignDtoIn;
import com.backandwhite.api.dto.in.GiftCardPurchaseDtoIn;
import com.backandwhite.api.dto.in.GiftCardRedeemDtoIn;
import com.backandwhite.api.dto.out.GiftCardDesignDtoOut;
import com.backandwhite.api.dto.out.GiftCardDtoOut;
import com.backandwhite.api.dto.out.GiftCardTransactionDtoOut;
import com.backandwhite.api.mapper.GiftCardApiMapper;
import com.backandwhite.api.util.PageableUtils;
import com.backandwhite.application.usecase.GiftCardUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import com.backandwhite.common.security.annotation.NxUser;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gift-cards")
@Tag(name = "GiftCards", description = "Endpoints para gestión de tarjetas de regalo")
public class GiftCardController {

    private final GiftCardUseCase giftCardUseCase;
    private final GiftCardApiMapper giftCardApiMapper;

    // ── Designs ──────────────────────────────────────────────────────────

    @GetMapping("/designs/active")
    @NxPublic
    @Operation(summary = "Listar diseños activos (público)")
    public ResponseEntity<List<GiftCardDesignDtoOut>> findActiveDesigns(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(giftCardApiMapper.toDesignDtoList(giftCardUseCase.findAllActiveDesigns()));
    }

    @GetMapping("/designs")
    @NxAdmin
    @Operation(summary = "[Admin] Listar todos los diseños")
    public ResponseEntity<List<GiftCardDesignDtoOut>> findAllDesigns(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        return ResponseEntity.ok(giftCardApiMapper.toDesignDtoList(giftCardUseCase.findAllDesigns()));
    }

    @GetMapping("/designs/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener diseño por ID")
    public ResponseEntity<GiftCardDesignDtoOut> findDesignById(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @PathVariable String id) {
        return ResponseEntity.ok(giftCardApiMapper.toDesignDto(giftCardUseCase.findDesignById(id)));
    }

    @PostMapping("/designs")
    @NxAdmin
    @Operation(summary = "[Admin] Crear diseño")
    public ResponseEntity<GiftCardDesignDtoOut> createDesign(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @Valid @RequestBody GiftCardDesignDtoIn dto) {
        GiftCardDesign created = giftCardUseCase.createDesign(giftCardApiMapper.toDesignDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCardApiMapper.toDesignDto(created));
    }

    @PutMapping("/designs/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Actualizar diseño")
    public ResponseEntity<GiftCardDesignDtoOut> updateDesign(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @PathVariable String id,
            @Valid @RequestBody GiftCardDesignDtoIn dto) {
        GiftCardDesign updated = giftCardUseCase.updateDesign(id, giftCardApiMapper.toDesignDomain(dto));
        return ResponseEntity.ok(giftCardApiMapper.toDesignDto(updated));
    }

    @DeleteMapping("/designs/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Eliminar diseño")
    public ResponseEntity<Void> deleteDesign(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        giftCardUseCase.deleteDesign(id);
        return ResponseEntity.noContent().build();
    }

    // ── GiftCards ───────────────────────────────────────────────────────

    @PostMapping("/purchase")
    @NxPublic
    @Operation(summary = "Comprar gift card", description = "Permite comprar una tarjeta regalo como invitado o como usuario autenticado. "
            + "Si hay sesión, buyerId se completa con el sub del JWT (X-Auth-Subject); "
            + "si no, la compra se registra sin buyerId (el destinatario aún la recibe por email).")
    public ResponseEntity<GiftCardDtoOut> purchase(
            @RequestHeader(value = AppConstants.HEADER_NX036_AUTH, required = false) String nxAuth,
            @RequestHeader(value = "X-Auth-Subject", required = false) String userId,
            @Valid @RequestBody GiftCardPurchaseDtoIn dto) {
        GiftCard card = giftCardApiMapper.toPurchaseDomain(dto);
        card.setBuyerId(userId);
        GiftCard created = giftCardUseCase.purchase(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCardApiMapper.toDto(created));
    }

    @GetMapping("/code/{code}")
    @NxUser
    @Operation(summary = "Consultar gift card por código")
    public ResponseEntity<GiftCardDtoOut> findByCode(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String code) {
        return ResponseEntity.ok(giftCardApiMapper.toDto(giftCardUseCase.findByCode(code)));
    }

    @PostMapping("/claim/{code}")
    @NxUser
    @Operation(summary = "Reclamar gift card por código (asociarla al usuario autenticado)")
    public ResponseEntity<GiftCardDtoOut> claimByCode(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = AppConstants.HEADER_AUTH_EMAIL, required = false) String email,
            @PathVariable String code) {
        GiftCard card = giftCardUseCase.claimByCode(code, email);
        return ResponseEntity.ok(giftCardApiMapper.toDto(card));
    }

    @GetMapping("/code/{code}/balance")
    @NxUser
    @Operation(summary = "Consultar balance de gift card")
    public ResponseEntity<BigDecimal> getBalance(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String code) {
        return ResponseEntity.ok(giftCardUseCase.getBalance(code).getAmount());
    }

    @PostMapping("/redeem")
    @NxUser
    @Operation(summary = "Canjear gift card")
    public ResponseEntity<GiftCardTransactionDtoOut> redeem(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @Valid @RequestBody GiftCardRedeemDtoIn dto) {
        GiftCardTransaction transaction = giftCardUseCase.redeem(dto.getCode(), Money.of(dto.getAmount()),
                dto.getOrderId());
        return ResponseEntity.ok(giftCardApiMapper.toTransactionDto(transaction));
    }

    @GetMapping("/my/sent")
    @NxUser
    @Operation(summary = "Mis tarjetas enviadas")
    public ResponseEntity<List<GiftCardDtoOut>> getMySent(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = AppConstants.HEADER_AUTH_SUBJECT, required = false) String userId) {
        List<GiftCard> cards = giftCardUseCase.findMySent(userId);
        return ResponseEntity.ok(cards.stream().map(giftCardApiMapper::toDto).toList());
    }

    @GetMapping("/my/received")
    @NxUser
    @Operation(summary = "Mis tarjetas recibidas")
    public ResponseEntity<List<GiftCardDtoOut>> getMyReceived(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @RequestHeader(value = AppConstants.HEADER_AUTH_EMAIL, required = false) String email) {
        List<GiftCard> cards = giftCardUseCase.findMyReceived(email);
        return ResponseEntity.ok(cards.stream().map(giftCardApiMapper::toDto).toList());
    }

    @GetMapping
    @NxAdmin
    @Operation(summary = "[Admin] Listar gift cards")
    public ResponseEntity<PaginationDtoOut<GiftCardDtoOut>> findAll(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @RequestParam(required = false) String status,
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {
        Map<String, Object> filters = new HashMap<>();
        if (status != null)
            filters.put("status", status);
        if (search != null)
            filters.put("search", search);
        PageResult<GiftCard> result = giftCardUseCase.findAll(filters, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, giftCardApiMapper::toDto));
    }

    @GetMapping("/{id}")
    @NxAdmin
    @Operation(summary = "[Admin] Obtener gift card por ID")
    public ResponseEntity<GiftCardDtoOut> findById(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String id) {
        return ResponseEntity.ok(giftCardApiMapper.toDto(giftCardUseCase.findById(id)));
    }

    @GetMapping("/{id}/transactions")
    @NxAdmin
    @Operation(summary = "[Admin] Listar transacciones de gift card")
    public ResponseEntity<List<GiftCardTransactionDtoOut>> findTransactions(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @PathVariable String id) {
        return ResponseEntity.ok(giftCardApiMapper.toTransactionDtoList(giftCardUseCase.findTransactions(id)));
    }

    @GetMapping("/buyer/{buyerId}")
    @NxAdmin
    @Operation(summary = "Gift cards por comprador")
    public ResponseEntity<PaginationDtoOut<GiftCardDtoOut>> findByBuyerId(
            @RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth, @PathVariable String buyerId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {
        PageResult<GiftCard> result = giftCardUseCase.findByBuyerId(buyerId, page, size, sortBy, ascending);
        return ResponseEntity.ok(PageableUtils.toResponse(result, giftCardApiMapper::toDto));
    }
}
