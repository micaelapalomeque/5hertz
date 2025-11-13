package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.MaterialPorOrden;

@Repository
public interface MaterialPorOrdenRepository extends JpaRepository<MaterialPorOrden, Integer> {
    
    List<MaterialPorOrden> findByIdOp(int idOp);
    
    Optional<MaterialPorOrden> findByIdOpAndSku(int idOp, String sku);
}