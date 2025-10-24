package com.proyecto_final.controller;

import com.proyecto_final.model.MovimientoStock;
import request.RegistrarMovimientoRequest;
import com.proyecto_final.service.MovimientoStockService;
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
    public void registrarMovimiento(@RequestBody RegistrarMovimientoRequest request) {
        movimientoStockService.registrarMovimiento(
            request.getIdAlmacen(),
            request.getSku(),
            request.getCantidad(),
            request.getTipoMovimiento()
        );
    }

    @GetMapping
    public List<MovimientoStock> consultarTodos() {
        return movimientoStockService.consultarTodos();
    }

    @GetMapping("/sku/{sku}")
    public List<MovimientoStock> consultarPorSku(@PathVariable String sku) {
        return movimientoStockService.consultarPorSku(sku);
    }

    @GetMapping("/egresos")
    public List<MovimientoStock> consultarEgresos() {
        return movimientoStockService.consultarEgresos();
    }

    @GetMapping("/ingresos")
    public List<MovimientoStock> consultarIngresos() {
        return movimientoStockService.consultarIngresos();
    }
}

