package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.LoteProceso;

@Repository
public interface LoteProcesoRepository extends JpaRepository<LoteProceso, Integer> {
    
    // Obtener lotes pendientes en una estación específica
    List<LoteProceso> findByEstacionActualAndEstado(String estacionActual, String estado);
    
    // Obtener todos los lotes de una orden
    List<LoteProceso> findByIdOp(int idOp);
    
    // Obtener lotes pendientes para un operario específico
    @Query("SELECT l FROM LoteProceso l WHERE l.estacionActual = :estacion AND l.estado = 'PENDIENTE'")
    List<LoteProceso> findLotesPendientesPorEstacion(@Param("estacion") String estacion);
}