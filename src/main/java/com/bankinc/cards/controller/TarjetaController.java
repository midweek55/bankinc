package com.bankinc.cards.controller;

import com.bankinc.cards.dto.ActivarTarjetaRequest;
import com.bankinc.cards.dto.RecargaSaldoRequest;
import com.bankinc.cards.model.Tarjeta;
import com.bankinc.cards.service.TarjetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@Tag(name = "Tarjetas", description = "API para la gestión de tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @Operation(
        summary = "Generar número de tarjeta",
        description = "Genera un nuevo número de tarjeta basado en el ID del producto"
    )
    @ApiResponse(responseCode = "200", description = "Número de tarjeta generado exitosamente")
    @GetMapping("/{productId}/number")
    public ResponseEntity<String> generarNumeroTarjeta(
            @Parameter(description = "ID del producto (6 dígitos)") @PathVariable String productId,
            @Parameter(description = "Nombre del titular") @RequestParam String titular) {
        return ResponseEntity.ok(tarjetaService.generarNumeroTarjeta(productId, titular));
    }

    @Operation(
        summary = "Activar tarjeta",
        description = "Activa una tarjeta existente"
    )
    @ApiResponse(responseCode = "200", description = "Tarjeta activada exitosamente")
    @PostMapping("/enroll")
    public ResponseEntity<Tarjeta> activarTarjeta(@RequestBody ActivarTarjetaRequest request) {
        return ResponseEntity.ok(tarjetaService.activarTarjeta(request));
    }

    @Operation(
        summary = "Bloquear tarjeta",
        description = "Bloquea una tarjeta existente"
    )
    @ApiResponse(responseCode = "204", description = "Tarjeta bloqueada exitosamente")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> bloquearTarjeta(
            @Parameter(description = "ID de la tarjeta") @PathVariable String cardId) {
        tarjetaService.bloquearTarjeta(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Recargar saldo",
        description = "Recarga el saldo de una tarjeta"
    )
    @ApiResponse(responseCode = "200", description = "Saldo recargado exitosamente")
    @PostMapping("/balance")
    public ResponseEntity<Tarjeta> recargarSaldo(@RequestBody RecargaSaldoRequest request) {
        return ResponseEntity.ok(tarjetaService.recargarSaldo(request));
    }

    @Operation(
        summary = "Consultar saldo",
        description = "Obtiene el saldo actual de una tarjeta"
    )
    @ApiResponse(responseCode = "200", description = "Saldo consultado exitosamente")
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<Double> consultarSaldo(
            @Parameter(description = "ID de la tarjeta") @PathVariable String cardId) {
        return ResponseEntity.ok(tarjetaService.consultarSaldo(cardId));
    }
}
