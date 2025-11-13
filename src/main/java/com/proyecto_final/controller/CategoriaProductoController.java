package com.proyecto_final.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.service.CategoriaProductoService;
import request.AgregarCategoriaRequest;

@RestController
@RequestMapping("/categorias")
public class CategoriaProductoController {

    private final CategoriaProductoService servicio;

    public CategoriaProductoController(CategoriaProductoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<?> agregarCategoria(@RequestBody AgregarCategoriaRequest request) {

        boolean ok = servicio.agregarCategoria(
                request.getNombre(),
                request.getDescripcion()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo crear la categoria. Verifica nombre, descripcion o duplicado.");
        }

        return ResponseEntity.ok("Categoria creada correctamente.");
    }

    @DeleteMapping("/{nombre}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable String nombre) {
        boolean ok = servicio.eliminarCategoria(nombre);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo eliminar la categoria. Puede no existir.");
        }

        return ResponseEntity.ok("Categoria eliminada correctamente.");
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProducto>> obtenerTodas() {
        return ResponseEntity.ok(servicio.obtenerTodas());
    }
}
