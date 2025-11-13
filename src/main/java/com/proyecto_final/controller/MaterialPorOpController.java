package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.*;
import com.proyecto_final.service.MaterialPorOpService;

@RestController
@RequestMapping("/material-por-op")
public class MaterialPorOpController {
    
    private final MaterialPorOpService materialPorOpService;
    
    public MaterialPorOpController(MaterialPorOpService materialPorOpService) {
        this.materialPorOpService = materialPorOpService;
    }
    
    @PutMapping("/registrar-desperdicio")
    public void registrarDesperdicio(@RequestBody RegistrarDesperdicioRequest request) {
        materialPorOpService.registrarDesperdicio(
            request.getIdOp(),
            request.getSku(),
            request.getCantidadDesperdiciada()
        );
    }
    
    public static class RegistrarDesperdicioRequest {
        private int idOp;
        private String sku;
        private int cantidadDesperdiciada;
        private String motivo;
        private String observaciones;
        private String estacion;
        private String operario;
        
        // Getters y setters
        public int getIdOp() { return idOp; }
        public void setIdOp(int idOp) { this.idOp = idOp; }
        
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        
        public int getCantidadDesperdiciada() { return cantidadDesperdiciada; }
        public void setCantidadDesperdiciada(int cantidadDesperdiciada) { this.cantidadDesperdiciada = cantidadDesperdiciada; }
        
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
        
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        
        public String getEstacion() { return estacion; }
        public void setEstacion(String estacion) { this.estacion = estacion; }
        
        public String getOperario() { return operario; }
        public void setOperario(String operario) { this.operario = operario; }
    }
}