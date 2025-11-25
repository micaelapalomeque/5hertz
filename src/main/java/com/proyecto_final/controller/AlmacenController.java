package com.proyecto_final.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.Almacen;
import com.proyecto_final.service.AlmacenService;
import com.proyecto_final.service.SectorTemplateService;
import com.proyecto_final.model.SectorTemplate;
import java.util.List;

import request.AlmacenRequest;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;
    private final SectorTemplateService sectorTemplateService;

    public AlmacenController(AlmacenService almacenService, SectorTemplateService sectorTemplateService) {
        this.almacenService = almacenService;
        this.sectorTemplateService = sectorTemplateService;
    }

    @PostMapping
    public ResponseEntity<?> crearAlmacen(@RequestBody AlmacenRequest request) {
        boolean ok = almacenService.crearAlmacen(
                request.getIdCentro(),
                request.getNombre(),
                request.getCapacidad(),
                request.getEstado()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo crear el almacen. Verifica los datos enviados.");
        }

        return ResponseEntity.ok("Almacen creado correctamente.");
    }

    @GetMapping
    public ResponseEntity<List<Almacen>> obtenerTodos() {
        return ResponseEntity.ok(almacenService.obtenerTodos());
    }

    @GetMapping("/{idAlmacen}/sectors")
    public ResponseEntity<List<SectorTemplate>> obtenerSectores(@PathVariable int idAlmacen) {
        List<SectorTemplate> sectores = sectorTemplateService.obtenerTodos();
        return ResponseEntity.ok(sectores);
    }
}
