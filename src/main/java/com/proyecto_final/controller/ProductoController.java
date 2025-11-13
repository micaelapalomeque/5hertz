package com.proyecto_final.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.model.Producto;
import com.proyecto_final.service.CategoriaProductoService;
import com.proyecto_final.service.ProductoService;

import request.ProductoRequest;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaProductoService categoriaProductoService;

    public ProductoController(
            ProductoService productoService,
            CategoriaProductoService categoriaProductoService) {
        this.productoService = productoService;
        this.categoriaProductoService = categoriaProductoService;
    }

    @PostMapping
    public ResponseEntity<?> altaProducto(@RequestBody ProductoRequest request) {

        if (request.getSku() == null || request.getSku().isBlank()) {
            return ResponseEntity.badRequest().body("El SKU es obligatorio.");
        }

        Optional<CategoriaProducto> opt = categoriaProductoService.findByNombre(request.getNombreCategoria());
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body("La categoria especificada no existe.");
        }

        CategoriaProducto categoria = opt.get();

        Producto producto = new Producto(
            request.getSku(),
            categoria.getIdCategoria(),
            request.getNombre(),
            request.getUnidadMedida(),
            request.getDescripcion()
        );

        boolean ok = productoService.altaProducto(producto);

        if (!ok) {
            return ResponseEntity.badRequest().body("No se pudo crear el producto. Verifica los datos enviados.");
        }

        return ResponseEntity.ok("Producto creado correctamente.");
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> bajaProducto(@PathVariable String sku) {
        if (sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest().body("El SKU es obligatorio.");
        }

        boolean ok = productoService.bajaProducto(sku);

        if (!ok) {
            return ResponseEntity.badRequest().body("El producto no existe o no pudo eliminarse.");
        }

        return ResponseEntity.ok("Producto eliminado correctamente.");
    }

    @GetMapping("/{sku}")
    public ResponseEntity<?> consultarPorSku(@PathVariable String sku) {
        if (sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest().body("El SKU es obligatorio.");
        }

        Optional<Producto> opt = productoService.getBySku(sku);

        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/disponibles-stock")
    public ResponseEntity<List<Producto>> obtenerProductosDisponiblesParaStock() {
        return ResponseEntity.ok(productoService.obtenerProductosDisponiblesParaStock());
    }
}
