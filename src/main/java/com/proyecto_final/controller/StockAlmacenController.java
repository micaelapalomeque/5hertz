package com.proyecto_final.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.service.StockAlmacenService;
import request.HabilitarProductoRequest;
import request.ModificarStockRequest;

@RestController
@RequestMapping("/stock")
public class StockAlmacenController {

    private final StockAlmacenService stockAlmacenService;

    public StockAlmacenController(StockAlmacenService stockAlmacenService) {
        this.stockAlmacenService = stockAlmacenService;
    }

    @PutMapping("/habilitar-producto")
    public ResponseEntity<?> habilitarProducto(@RequestBody HabilitarProductoRequest request) {
        boolean ok = stockAlmacenService.habilitarProducto(
                request.getIdAlmacen(),
                request.getSku()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo habilitar el producto. Verifica idAlmacen y sku.");
        }

        return ResponseEntity.ok("Producto habilitado correctamente.");
    }

    @PutMapping("/ingresar")
    public ResponseEntity<?> ingresarStock(@RequestBody ModificarStockRequest request) {
        boolean ok = stockAlmacenService.ingresarMaterial(
                request.getSku(),
                request.getIdAlmacen(),
                request.getCantidad()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo ingresar el stock. Verifica sku, idAlmacen y cantidad.");
        }

        return ResponseEntity.ok("Stock ingresado correctamente.");
    }

    @PutMapping("/retirar")
    public ResponseEntity<?> retirarStock(@RequestBody ModificarStockRequest request) {
        boolean ok = stockAlmacenService.retirarMaterial(
                request.getSku(),
                request.getIdAlmacen(),
                request.getCantidad()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo retirar el stock. Verifica stock disponible, sku, idAlmacen y cantidad.");
        }

        return ResponseEntity.ok("Stock retirado correctamente.");
    }

    @GetMapping("/consultar-almacen/{idAlmacen}")
    public ResponseEntity<List<StockAlmacen>> consultarStockAlmacen(@PathVariable int idAlmacen) {
        if (idAlmacen <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<StockAlmacen> stock = stockAlmacenService.consultarStockAlmacen(idAlmacen);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/consultar-producto-almacen")
    public ResponseEntity<?> consultarStockProducto(
            @RequestParam String sku,
            @RequestParam int idAlmacen) {

        StockAlmacen registro = stockAlmacenService.consultarStockProducto(sku, idAlmacen);

        if (registro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(registro);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<StockAlmacen>> listarStock() {
        List<StockAlmacen> stock = stockAlmacenService.listarStock();
        return ResponseEntity.ok(stock);
    }

    @PutMapping("/actualizar-minimo")
    public ResponseEntity<?> actualizarCantidadMinima(@RequestBody ModificarStockRequest request) {
        boolean ok = stockAlmacenService.modificarCantidadMinima(
                request.getSku(),
                request.getIdAlmacen(),
                request.getCantidad()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo actualizar la cantidad minima. Verifica sku, idAlmacen y cantidad.");
        }

        return ResponseEntity.ok("Cantidad minima actualizada correctamente.");
    }
}

