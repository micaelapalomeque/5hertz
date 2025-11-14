package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.MaterialPorOrden;

@Repository
public interface MaterialPorOrdenRepository extends JpaRepository<MaterialPorOrden, Integer> {
    
    List<MaterialPorOrden> findByIdOp(int idOp);
    
    @Query("SELECT DISTINCT m.idOp, o.sku, o.estado, SUM(m.cantidadDesperdiciada) " +
           "FROM MaterialPorOrden m JOIN OrdenProduccion o ON m.idOp = o.idOp " +
           "WHERE o.estado IN ('ACTIVA', 'TERMINADA') " +
           "GROUP BY m.idOp, o.sku, o.estado ORDER BY m.idOp DESC")
    List<Object[]> findOrdenesConDesperdicio();
    
    Optional<MaterialPorOrden> findByIdOpAndSku(int idOp, String sku);
}