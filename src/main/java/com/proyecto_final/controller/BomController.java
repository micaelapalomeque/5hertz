package com.proyecto_final.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.Bom;
import com.proyecto_final.model.Producto;
import com.proyecto_final.service.BomService;

import request.BomRequest;

@RestController
@RequestMapping("/bom")
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    @PostMapping
    public ResponseEntity<?> crearBom(@RequestBody BomRequest request) {

        boolean ok = bomService.crearBom(
                request.getSkuProductoFinal(),
                request.getSkuMaterial(),
                request.getCantPorUnidad()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo crear la BOM. Verifica SKU final, SKU material y cantidad por unidad.");
        }

        return ResponseEntity.ok("BOM creada correctamente.");
    }

    @GetMapping("/{skuProductoFinal}")
    public ResponseEntity<?> obtenerBom(@PathVariable String skuProductoFinal) {
        if (skuProductoFinal == null || skuProductoFinal.isBlank()) {
            return ResponseEntity.badRequest().body("El SKU del producto final es obligatorio.");
        }

        List<Bom> lista = bomService.obtenerListaMateriales(skuProductoFinal);

        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{skuProductoFinal}")
    public ResponseEntity<?> eliminarBom(@PathVariable String skuProductoFinal) {

        boolean ok = bomService.eliminarBomProducto(skuProductoFinal);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo eliminar la BOM. Verifica el SKU del producto final.");
        }

        return ResponseEntity.ok("BOM eliminada correctamente.");
    }

    @GetMapping("/productos-fabricables")
    public ResponseEntity<List<Producto>> obtenerProductosFabricables() {
        return ResponseEntity.ok(bomService.obtenerProductosFabricables());
    }
}
