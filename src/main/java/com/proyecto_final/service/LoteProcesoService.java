package com.proyecto_final.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.LoteProceso;
import com.proyecto_final.model.EtapaProceso;
import com.proyecto_final.repository.LoteProcesoRepository;
import com.proyecto_final.repository.EtapaProcesoRepository;

@Service
public class LoteProcesoService {
    
    private final LoteProcesoRepository loteProcesoRepository;
    private final EtapaProcesoRepository etapaProcesoRepository;
    
    public LoteProcesoService(LoteProcesoRepository loteProcesoRepository, EtapaProcesoRepository etapaProcesoRepository) {
        this.loteProcesoRepository = loteProcesoRepository;
        this.etapaProcesoRepository = etapaProcesoRepository;
    }
    
    // Método simplificado: crear lotes directamente sin etapas
    public void crearLotesIniciales(int idOp, int cantidadTotal, int tamanoLote) {
        System.out.println("=== CREANDO LOTES DIRECTOS ===");
        System.out.println("ID OP: " + idOp);
        System.out.println("Cantidad total: " + cantidadTotal);
        System.out.println("Tamaño lote: " + tamanoLote);
        
        try {
            // Crear etapa LAVADO para esta orden
            EtapaProceso etapaLavado = new EtapaProceso();
            etapaLavado.setIdProceso(1);
            etapaLavado.setOrden(1);
            etapaLavado.setNombreEtapa("LAVADO");
            etapaLavado.setDescripcion("Lavado y desinfección");
            etapaLavado.setIdOp(idOp);
            etapaLavado.setCantidadTotalUnidades(cantidadTotal);
            etapaLavado.setCantidadPendienteUnidades(cantidadTotal);
            etapaLavado.setEstado("PENDIENTE");
            etapaLavado.setFechaInicio(LocalDateTime.now());
            
            EtapaProceso etapaGuardada = etapaProcesoRepository.save(etapaLavado);
            System.out.println("Etapa LAVADO creada con ID: " + etapaGuardada.getIdEtapa());
            
            // Crear lotes para esta etapa
            int lotesPorCrear = Math.max(1, (int) Math.ceil((double) cantidadTotal / tamanoLote));
            System.out.println("Lotes a crear: " + lotesPorCrear);
            
            for (int i = 0; i < lotesPorCrear; i++) {
                int unidadesEnEsteLote = Math.min(tamanoLote, cantidadTotal - (i * tamanoLote));
                
                LoteProceso lote = new LoteProceso();
                lote.setIdEtapa(etapaGuardada.getIdEtapa());
                lote.setIdOp(idOp);
                lote.setUnidadesLote(unidadesEnEsteLote);
                lote.setEstado("EN_PROCESO");
                lote.setEstacionActual("LAVADO");
                lote.setFechaInicio(LocalDateTime.now());
                
                LoteProceso loteGuardado = loteProcesoRepository.save(lote);
                System.out.println("Lote " + (i+1) + " guardado - ID: " + loteGuardado.getIdLote() + ", Unidades: " + unidadesEnEsteLote);
            }
            
            System.out.println("=== LOTES CREADOS EXITOSAMENTE ===");
            
        } catch (Exception e) {
            System.err.println("ERROR CREANDO LOTES: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Obtener lotes pendientes para una estación
    public List<LoteProceso> obtenerLotesPendientes(String estacion) {
        return loteProcesoRepository.findLotesPendientesPorEstacion(estacion);
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
        System.out.println("=== COMPLETANDO LOTE ===");
        System.out.println("ID Lote: " + idLote);
        System.out.println("ID Operario: " + idOperario);
        
        LoteProceso lote = loteProcesoRepository.findById(idLote)
            .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
        
        System.out.println("Lote encontrado - ID: " + lote.getIdLote());
        System.out.println("Estación actual: " + lote.getEstacionActual());
        System.out.println("Estado actual: " + lote.getEstado());
        
        // Obtener siguiente estación
        String siguienteEstacion = obtenerSiguienteEtapa(lote.getEstacionActual());
        System.out.println("Siguiente estación: " + siguienteEstacion);
        
        if (siguienteEstacion != null) {
            // Transferir a siguiente estación
            lote.setEstacionActual(siguienteEstacion);
            lote.setEstado("EN_PROCESO");
            System.out.println("Transfiriendo a: " + siguienteEstacion);
        } else {
            // No hay más estaciones, completar
            lote.setEstado("COMPLETADO");
            lote.setFechaFin(LocalDateTime.now());
            System.out.println("Completando lote (última estación)");
        }
        
        lote.setOperarioResponsable(idOperario);
        LoteProceso loteGuardado = loteProcesoRepository.save(lote);
        
        System.out.println("Lote guardado - ID: " + loteGuardado.getIdLote());
        System.out.println("Nueva estación: " + loteGuardado.getEstacionActual());
        System.out.println("Nuevo estado: " + loteGuardado.getEstado());
        System.out.println("=== FIN COMPLETAR LOTE ===");
    }
    
    // Flujo fijo de etapas
    private String obtenerSiguienteEtapa(String etapaActual) {
        switch (etapaActual) {
            case "LAVADO": return "CLASIFICACION";
            case "CLASIFICACION": return "PELADO_TROZADO";
            case "PELADO_TROZADO": return "ESCURRIDO";
            case "ESCURRIDO": return "CONGELACION";
            case "CONGELACION": return "EMPAQUETADO";
            case "EMPAQUETADO": return null;
            default: return null;
        }
    }
    
    private int obtenerOrdenEtapa(String nombreEtapa) {
        switch (nombreEtapa) {
            case "LAVADO": return 1;
            case "CLASIFICACION": return 2;
            case "PELADO": return 3;
            case "ESCURRIDO": return 4;
            case "CONGELACION": return 5;
            case "EMPAQUETADO": return 6;
            default: return 1;
        }
    }
}