package com.proyecto_final.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.LoteProceso;
import com.proyecto_final.repository.LoteProcesoRepository;

@Service
public class LoteProcesoService {
    
    private final LoteProcesoRepository loteProcesoRepository;
    
    public LoteProcesoService(LoteProcesoRepository loteProcesoRepository) {
        this.loteProcesoRepository = loteProcesoRepository;
    }
    
    // Crear lotes iniciales para una orden (en estación LAVADO)
    public void crearLotesIniciales(int idOp, int cantidadTotal, int tamanoLote) {
        int lotesPorCrear = (int) Math.ceil((double) cantidadTotal / tamanoLote);
        
        for (int i = 0; i < lotesPorCrear; i++) {
            int unidadesEnEsteLote = Math.min(tamanoLote, cantidadTotal - (i * tamanoLote));
            
            LoteProceso lote = new LoteProceso(idOp, "LAVADO", unidadesEnEsteLote);
            loteProcesoRepository.save(lote);
        }
    }
    
    // Obtener lotes pendientes para una estación
    public List<LoteProceso> obtenerLotesPendientes(String estacion) {
        return loteProcesoRepository.findLotesPendientesPorEstacion(estacion);
    }
    
    // Obtener todos los lotes de una orden
    public List<LoteProceso> obtenerLotesPorOrden(int idOp) {
        return loteProcesoRepository.findByIdOp(idOp);
    }
    
    // Completar lote y enviarlo a siguiente estación
    public void completarLote(int idLote, int idOperario) {
        LoteProceso lote = loteProcesoRepository.findById(idLote)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
        
        // Marcar lote actual como completado
        lote.setEstado("COMPLETADO");
        lote.setFechaFin(LocalDateTime.now());
        lote.setOperarioResponsable(idOperario);
        loteProcesoRepository.save(lote);
        
        // Crear lote en siguiente estación
        String siguienteEstacion = obtenerSiguienteEstacion(lote.getEstacionActual());
        if (siguienteEstacion != null) {
            LoteProceso nuevoLote = new LoteProceso(
                lote.getIdOp(), 
                siguienteEstacion, 
                lote.getUnidadesLote()
            );
            loteProcesoRepository.save(nuevoLote);
        }
    }
    
    // Flujo fijo de estaciones
    private String obtenerSiguienteEstacion(String estacionActual) {
        switch (estacionActual) {
            case "LAVADO": return "CLASIFICACION";
            case "CLASIFICACION": return "PELADO";
            case "PELADO": return "ESCURRIDO";
            case "ESCURRIDO": return "CONGELACION";
            case "CONGELACION": return "EMPAQUETADO";
            case "EMPAQUETADO": return null; // Última estación
            default: return null;
        }
    }
}