package com.backandwhite.api.controller;

import com.backandwhite.api.dto.in.CurrencyRateToggleDtoIn;
import com.backandwhite.api.dto.out.ConvertResultDtoOut;
import com.backandwhite.api.dto.out.CurrencyRateDtoOut;
import com.backandwhite.api.dto.out.SyncResultDtoOut;
import com.backandwhite.api.mapper.CurrencyRateApiMapper;
import com.backandwhite.application.usecase.CurrencyRateUseCase;
import com.backandwhite.common.constants.AppConstants;
import com.backandwhite.common.security.annotation.NxAdmin;
import com.backandwhite.common.security.annotation.NxPublic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency-rates")
@Tag(name = "Currency Rates", description = "Endpoints para gestión de tasas de cambio")
public class CurrencyRateController {

    private final CurrencyRateUseCase useCase;
    private final CurrencyRateApiMapper mapper;

    @GetMapping
    @NxPublic
    @Operation(summary = "[Public] Listar tasas de cambio")
    public ResponseEntity<List<CurrencyRateDtoOut>> findAll(@RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(mapper.toDtoList(useCase.findAll(active)));
    }

    @GetMapping("/{code}")
    @NxPublic
    @Operation(summary = "[Public] Obtener tasa por código de moneda")
    public ResponseEntity<CurrencyRateDtoOut> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(mapper.toDto(useCase.findByCode(code)));
    }

    @PatchMapping("/{code}/active")
    @NxAdmin
    @Operation(summary = "[Admin] Activar/desactivar moneda")
    public ResponseEntity<CurrencyRateDtoOut> toggleActive(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth,
            @PathVariable String code, @Valid @RequestBody CurrencyRateToggleDtoIn dto) {
        return ResponseEntity.ok(mapper.toDto(useCase.toggleActive(code, dto.getActive())));
    }

    @PostMapping("/sync")
    @NxAdmin
    @Operation(summary = "[Admin] Sincronizar tasas desde CurrencyLayer API")
    public ResponseEntity<SyncResultDtoOut> sync(@RequestHeader(AppConstants.HEADER_NX036_AUTH) String nxAuth) {
        int count = useCase.syncFromApi();
        return ResponseEntity.ok(SyncResultDtoOut.builder().ratesUpdated(count).syncedAt(Instant.now()).build());
    }

    @GetMapping("/convert")
    @NxPublic
    @Operation(summary = "[Public] Convertir monto entre monedas")
    public ResponseEntity<ConvertResultDtoOut> convert(@RequestParam BigDecimal amount, @RequestParam String from,
            @RequestParam String to) {
        BigDecimal result = useCase.convert(amount, from, to);

        var fromRate = useCase.findByCode(from);
        var toRate = useCase.findByCode(to);
        BigDecimal exchangeRate = toRate.getRate().divide(fromRate.getRate(), 8, RoundingMode.HALF_UP);

        return ResponseEntity.ok(ConvertResultDtoOut.builder().originalAmount(amount).fromCurrency(from.toUpperCase())
                .toCurrency(to.toUpperCase()).convertedAmount(result).exchangeRate(exchangeRate).build());
    }
}
