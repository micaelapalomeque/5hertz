package com.proyecto_final.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.CentroProduccion;
import com.proyecto_final.service.CentroProduccionService;

import request.CentroProduccionRequest;

@RestController
@RequestMapping("/centros-produccion")
public class CentroProduccionController {

    private final CentroProduccionService centroProduccionService;

    public CentroProduccionController(CentroProduccionService centroProduccionService) {
        this.centroProduccionService = centroProduccionService;
    }

    @PostMapping
    public ResponseEntity<?> crearCentro(@RequestBody CentroProduccionRequest request) {

        boolean ok = centroProduccionService.crearCentro(
                request.getSucursal(),
                request.getDescripcion()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo crear el centro de produccion. Verifica sucursal y descripcion.");
        }

        return ResponseEntity.ok("Centro de produccion creado correctamente.");
    }

    @GetMapping
    public ResponseEntity<List<CentroProduccion>> obtenerTodos() {
        return ResponseEntity.ok(centroProduccionService.obtenerTodos());
    }
}
