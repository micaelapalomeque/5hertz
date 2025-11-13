package com.proyecto_final.controller;

import com.proyecto_final.model.MovimientoStock;
import request.RegistrarMovimientoRequest;
import com.proyecto_final.service.MovimientoStockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos-stock")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    public MovimientoStockController(MovimientoStockService movimientoStockService) {
        this.movimientoStockService = movimientoStockService;
    }

    @PostMapping
    public ResponseEntity<?> registrarMovimiento(@RequestBody RegistrarMovimientoRequest request) {

        if (request.getIdAlmacen() <= 0 ||
            request.getSku() == null || request.getSku().isBlank() ||
            request.getCantidad() <= 0 ||
            request.getTipoMovimiento() == null || request.getTipoMovimiento().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Datos invalidos: idAlmacen > 0, sku no vacio, cantidad > 0, tipoMovimiento no vacio.");
        }

        boolean ok = movimientoStockService.registrarMovimiento(
                request.getIdAlmacen(),
                request.getSku(),
                request.getCantidad(),
                request.getTipoMovimiento()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo registrar el movimiento. Verifica los datos enviados.");
        }

        return ResponseEntity.ok("Movimiento registrado correctamente.");
    }

    @GetMapping
    public ResponseEntity<List<MovimientoStock>> consultarTodos() {
        return ResponseEntity.ok(movimientoStockService.consultarTodos());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<?> consultarPorSku(@PathVariable String sku) {
        if (sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest().body("El SKU es obligatorio.");
        }

        List<MovimientoStock> lista = movimientoStockService.consultarPorSku(sku);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/egresos")
    public ResponseEntity<List<MovimientoStock>> consultarEgresos() {
        return ResponseEntity.ok(movimientoStockService.consultarEgresos());
    }

    @GetMapping("/ingresos")
    public ResponseEntity<List<MovimientoStock>> consultarIngresos() {
        return ResponseEntity.ok(movimientoStockService.consultarIngresos());
    }
}


