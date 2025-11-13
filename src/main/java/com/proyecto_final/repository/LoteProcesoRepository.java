package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.LoteProceso;

@Repository
public interface LoteProcesoRepository extends JpaRepository<LoteProceso, Integer> {
    
    // Obtener lotes por etapa
    List<LoteProceso> findByIdEtapa(int idEtapa);
    
    // Obtener lotes pendientes por estación
    List<LoteProceso> findByEstacionActualAndEstado(String estacionActual, String estado);
    
    // Método alternativo usando query
    @Query("SELECT l FROM LoteProceso l WHERE l.estacionActual = :estacion AND l.estado = 'EN_PROCESO'")
    List<LoteProceso> findLotesPendientesPorEstacion(@Param("estacion") String estacion);
    
    // Método alternativo más simple para debug
    @Query("SELECT l FROM LoteProceso l WHERE l.estado = 'EN_PROCESO'")
    List<LoteProceso> findAllLotesPendientes();
}