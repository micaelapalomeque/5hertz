package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.EtapaProceso;

@Repository
public interface EtapaProcesoRepository extends JpaRepository<EtapaProceso, Integer> {
    
    // Obtener etapas por proceso (catálogo fijo)
    List<EtapaProceso> findByIdProcesoOrderByOrden(int idProceso);
    
    // Obtener etapa por nombre
    EtapaProceso findByNombreEtapa(String nombreEtapa);
}