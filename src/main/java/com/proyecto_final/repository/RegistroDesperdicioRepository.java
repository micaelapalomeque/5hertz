package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.RegistroDesperdicio;

@Repository
public interface RegistroDesperdicioRepository extends JpaRepository<RegistroDesperdicio, Integer> {
    
    List<RegistroDesperdicio> findByIdOp(int idOp);
    
    List<RegistroDesperdicio> findByEstacion(String estacion);
    
    @Query("SELECT r.motivo, COUNT(r) FROM RegistroDesperdicio r GROUP BY r.motivo ORDER BY COUNT(r) DESC")
    List<Object[]> findMotivosMasFrecuentes();
}