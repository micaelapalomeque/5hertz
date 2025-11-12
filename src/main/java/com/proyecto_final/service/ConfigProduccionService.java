package com.proyecto_final.service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.ConfigProduccion;
import com.proyecto_final.repository.ConfigProduccionRepository;

@Service
public class ConfigProduccionService {
    
    private final ConfigProduccionRepository configRepository;
    
    public ConfigProduccionService(ConfigProduccionRepository configRepository) {
        this.configRepository = configRepository;
    }
    
    // Obtener configuración actual
    public ConfigProduccion obtenerConfiguracion() {
        Optional<ConfigProduccion> config = configRepository.findConfiguracionActual();
        
        if (config.isPresent()) {
            return config.get();
        } else {
            // Si no existe configuración, crear una por defecto
            ConfigProduccion configDefault = new ConfigProduccion(500, 10, "SISTEMA");
            return configRepository.save(configDefault);
        }
    }
    
    // Actualizar configuración
    public ConfigProduccion actualizarConfiguracion(int cantidadBase, int numeroLotes, String modificadoPor) {
        Optional<ConfigProduccion> configExistente = configRepository.findConfiguracionActual();
        
        ConfigProduccion config;
        if (configExistente.isPresent()) {
            // Actualizar configuración existente
            config = configExistente.get();
            config.setCantidadBaseOrden(cantidadBase);
            config.setNumeroLotesFijo(numeroLotes);
            config.setModificadoPor(modificadoPor);
            config.setFechaModificacion(LocalDateTime.now());
        } else {
            // Crear nueva si no existe
            config = new ConfigProduccion(cantidadBase, numeroLotes, modificadoPor);
        }
        
        return configRepository.save(config);
    }
}