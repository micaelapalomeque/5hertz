package com.proyecto_final.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.Almacen;
import com.proyecto_final.service.AlmacenService;
import request.AlmacenRequest;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController {
    
    private final AlmacenService almacenService;
    
    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }
    
    @PostMapping
    public void crearAlmacen(@RequestBody AlmacenRequest request) {
        almacenService.crearAlmacen(request.getIdCentro(), request.getNombre(), 
                                   request.getCapacidad(), request.getEstado());
    }
    
    @GetMapping
    public List<Almacen> obtenerTodos() {
        return almacenService.obtenerTodos();
    }
}