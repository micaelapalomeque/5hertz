package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.ResumenDesperdicio;

@Repository
public interface ResumenDesperdicioRepository extends JpaRepository<ResumenDesperdicio, Integer> {
    
    Optional<ResumenDesperdicio> findByIdOp(int idOp);
    
    @Query("SELECT r.skuMayorDesperdicio, SUM(r.gramosDesperdiciados) FROM ResumenDesperdicio r GROUP BY r.skuMayorDesperdicio ORDER BY SUM(r.gramosDesperdiciados) DESC")
    List<Object[]> findTop3ByOrderByGramosDesc();
    
    @Query("SELECT r.motivoPrincipal, COUNT(r) FROM ResumenDesperdicio r GROUP BY r.motivoPrincipal ORDER BY COUNT(r) DESC")
    List<Object[]> findMotivosMasFrecuentesResumen();
}