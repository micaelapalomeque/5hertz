package com.proyecto_final.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void crearCentro(@RequestBody CentroProduccionRequest request) {
        centroProduccionService.crearCentro(request.getSucursal(), request.getDescripcion());
    }
    
    @GetMapping
    public List<CentroProduccion> obtenerTodos() {
        return centroProduccionService.obtenerTodos();
    }
}