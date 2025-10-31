package com.proyecto_final.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.Bom;
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
    public void crearBom(@RequestBody BomRequest request) {
        bomService.crearBom(request.getSkuProductoFinal(), request.getSkuMaterial(), request.getCantPorUnidad());
    }
    
    @GetMapping("/{sku}")
    public List<Bom> obtenerBom(@PathVariable String sku) {
        return bomService.obtenerListaMateriales(sku);
    }
    
    @DeleteMapping("/{sku}")
    public void eliminarBom(@PathVariable String sku) {
        bomService.eliminarBomProducto(sku);
    }
}