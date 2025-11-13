package com.proyecto_final.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.LoteProceso;
import com.proyecto_final.repository.LoteProcesoRepository;

@Service
public class LoteProcesoService {
    
    private final LoteProcesoRepository loteProcesoRepository;
    
    private final OrdenProduccionService ordenProduccionService;
    
    public LoteProcesoService(LoteProcesoRepository loteProcesoRepository, @Lazy OrdenProduccionService ordenProduccionService) {
        this.loteProcesoRepository = loteProcesoRepository;
        this.ordenProduccionService = ordenProduccionService;
    }
    
    // Crear lotes iniciales para una orden (en estación LAVADO)
    public void crearLotesIniciales(int idOp, int cantidadTotal, int tamanoLote) {
        int lotesPorCrear = (int) Math.ceil((double) cantidadTotal / tamanoLote);
        
        for (int i = 0; i < lotesPorCrear; i++) {
            int unidadesEnEsteLote = Math.min(tamanoLote, cantidadTotal - (i * tamanoLote));
            
            LoteProceso lote = new LoteProceso();
            lote.setIdOp(idOp);
            lote.setUnidadesLote(unidadesEnEsteLote);
            lote.setEstacionActual("LAVADO");
            lote.setEstado("EN_PROCESO");
            lote.setFechaInicio(LocalDateTime.now());
            lote.setIdEtapa(1); // Usar etapa por defecto
            
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
    
    // Obtener lotes por etapa
    public List<LoteProceso> obtenerLotesPorEtapa(int idEtapa) {
        return loteProcesoRepository.findByIdEtapa(idEtapa);
    }
    
    // Debug: obtener todos los lotes
    public List<LoteProceso> obtenerTodosLosLotes() {
        return loteProcesoRepository.findAll();
    }
    
    // Completar lote y transferir a siguiente estación
    public void completarLote(int idLote, int idOperario) {
        LoteProceso lote = loteProcesoRepository.findById(idLote)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
        
        // Obtener siguiente estación
        String siguienteEstacion = obtenerSiguienteEstacion(lote.getEstacionActual());
        
        if (siguienteEstacion != null) {
            // Transferir a siguiente estación
            lote.setEstacionActual(siguienteEstacion);
            lote.setEstado("EN_PROCESO");
        } else {
            // No hay más estaciones, completar
            lote.setEstado("COMPLETADO");
            lote.setFechaFin(LocalDateTime.now());
        }
        
        lote.setOperarioResponsable(idOperario);
        loteProcesoRepository.save(lote);
        
        // Verificar DESPUÉS del save si el lote se completó
        if (siguienteEstacion == null) {
            verificarYFinalizarOrden(lote.getIdOp());
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
    
    // Verificar si todos los lotes están completados y finalizar orden
    private void verificarYFinalizarOrden(int idOp) {
        System.out.println("=== VERIFICANDO ORDEN " + idOp + " ===");
        
        List<LoteProceso> todosLosLotes = loteProcesoRepository.findByIdOp(idOp);
        List<LoteProceso> lotesCompletados = loteProcesoRepository.findByIdOpAndEstado(idOp, "COMPLETADO");
        
        System.out.println("Lotes totales: " + todosLosLotes.size());
        System.out.println("Lotes completados: " + lotesCompletados.size());
        
        if (todosLosLotes.size() == lotesCompletados.size() && todosLosLotes.size() > 0) {
            System.out.println("¡TODOS LOS LOTES COMPLETADOS! Consumiendo orden...");
            ordenProduccionService.consumirOp(idOp, "SISTEMA_AUTOMATICO");
            System.out.println("Orden " + idOp + " marcada como CONSUMIDA");
        } else {
            System.out.println("Orden aún no completa. Faltan: " + (todosLosLotes.size() - lotesCompletados.size()) + " lotes");
        }
    }
}