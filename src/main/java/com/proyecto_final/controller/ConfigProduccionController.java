package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.*;
import com.proyecto_final.model.ConfigProduccion;
import com.proyecto_final.service.ConfigProduccionService;

@RestController
@RequestMapping("/config-produccion")
public class ConfigProduccionController {
    
    private final ConfigProduccionService configService;
    
    public ConfigProduccionController(ConfigProduccionService configService) {
        this.configService = configService;
    }
    
    // Obtener configuración actual
    @GetMapping
    public ConfigProduccion obtenerConfiguracion() {
        return configService.obtenerConfiguracion();
    }
    
    // Actualizar configuración
    @PutMapping
    public ConfigProduccion actualizarConfiguracion(@RequestBody ConfigRequest request) {
        return configService.actualizarConfiguracion(
            request.getCantidadBaseOrden(),
            request.getNumeroLotesFijo(),
            request.getModificadoPor()
        );
    }
    
    // Clase interna para el request
    public static class ConfigRequest {
        private int cantidadBaseOrden;
        private int numeroLotesFijo;
        private String modificadoPor;
        
        // Getters y setters
        public int getCantidadBaseOrden() { return cantidadBaseOrden; }
        public void setCantidadBaseOrden(int cantidadBaseOrden) { this.cantidadBaseOrden = cantidadBaseOrden; }
        
        public int getNumeroLotesFijo() { return numeroLotesFijo; }
        public void setNumeroLotesFijo(int numeroLotesFijo) { this.numeroLotesFijo = numeroLotesFijo; }
        
        public String getModificadoPor() { return modificadoPor; }
        public void setModificadoPor(String modificadoPor) { this.modificadoPor = modificadoPor; }
    }
}