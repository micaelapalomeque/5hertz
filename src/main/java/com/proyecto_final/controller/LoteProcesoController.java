package com.proyecto_final.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.proyecto_final.model.LoteProceso;
import com.proyecto_final.service.LoteProcesoService;

@RestController
@RequestMapping("/lotes")
public class LoteProcesoController {
    
    private final LoteProcesoService loteProcesoService;
    
    public LoteProcesoController(LoteProcesoService loteProcesoService) {
        this.loteProcesoService = loteProcesoService;
    }
    
    // Obtener lotes pendientes para una estación específica
    @GetMapping("/estacion/{estacion}")
    public List<LoteProceso> obtenerLotesPorEstacion(@PathVariable String estacion) {
        return loteProcesoService.obtenerLotesPendientes(estacion);
    }
    
    // Completar un lote y enviarlo a la siguiente estación
    @PutMapping("/completar/{idLote}")
    public void completarLote(@PathVariable int idLote, @RequestParam int idOperario) {
        loteProcesoService.completarLote(idLote, idOperario);
    }
    
    // Obtener lotes por etapa
    @GetMapping("/etapa/{idEtapa}")
    public List<LoteProceso> obtenerLotesPorEtapa(@PathVariable int idEtapa) {
        return loteProcesoService.obtenerLotesPorEtapa(idEtapa);
    }
    
    // Debug: obtener todos los lotes
    @GetMapping("/debug/todos")
    public List<LoteProceso> obtenerTodosLosLotes() {
        return loteProcesoService.obtenerTodosLosLotes();
    }
}