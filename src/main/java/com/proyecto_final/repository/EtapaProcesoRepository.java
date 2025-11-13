package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.EtapaProceso;

@Repository
public interface EtapaProcesoRepository extends JpaRepository<EtapaProceso, Integer> {
    
    // Obtener etapas por orden de producción
    List<EtapaProceso> findByIdOp(int idOp);
    
    // Obtener etapas pendientes por nombre de etapa
    @Query("SELECT e FROM EtapaProceso e WHERE e.nombreEtapa = :nombreEtapa AND e.estado = 'PENDIENTE'")
    List<EtapaProceso> findEtapasPendientesPorNombre(@Param("nombreEtapa") String nombreEtapa);
}