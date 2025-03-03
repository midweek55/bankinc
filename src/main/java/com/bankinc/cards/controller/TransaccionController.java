package com.bankinc.cards.controller;

import com.bankinc.cards.dto.AnulacionRequest;
import com.bankinc.cards.dto.CompraRequest;
import com.bankinc.cards.model.Transaccion;
import com.bankinc.cards.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transacciones", description = "API para la gestión de transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @Operation(
        summary = "Realizar compra",
        description = "Procesa una nueva transacción de compra"
    )
    @ApiResponse(responseCode = "200", description = "Compra realizada exitosamente")
    @PostMapping("/purchase")
    public ResponseEntity<Transaccion> realizarCompra(@RequestBody CompraRequest request) {
        return ResponseEntity.ok(transaccionService.realizarCompra(request));
    }

    @Operation(
        summary = "Consultar transacción",
        description = "Obtiene los detalles de una transacción específica"
    )
    @ApiResponse(responseCode = "200", description = "Transacción consultada exitosamente")
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaccion> consultarTransaccion(
            @Parameter(description = "ID de la transacción") @PathVariable String transactionId) {
        return ResponseEntity.ok(transaccionService.consultarTransaccion(transactionId));
    }

    @Operation(
        summary = "Anular transacción",
        description = "Anula una transacción existente (solo disponible dentro de las 24 horas siguientes)"
    )
    @ApiResponse(responseCode = "200", description = "Transacción anulada exitosamente")
    @PostMapping("/anulation")
    public ResponseEntity<Transaccion> anularTransaccion(@RequestBody AnulacionRequest request) {
        return ResponseEntity.ok(transaccionService.anularTransaccion(request));
    }
}
