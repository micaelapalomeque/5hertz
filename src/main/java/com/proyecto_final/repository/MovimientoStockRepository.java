package com.proyecto_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.MovimientoStock;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Integer> {
	
	List<MovimientoStock> findByTipoMovimiento(String tipoMovimiento);
	
	List<MovimientoStock> findBySku(String sku);
}