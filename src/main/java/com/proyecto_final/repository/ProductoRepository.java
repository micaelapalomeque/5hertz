package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
	Optional<Producto> findBySku(String sku);
	
	@Query("SELECT p FROM Producto p WHERE p.sku NOT IN (SELECT s.sku FROM StockAlmacen s)")
	List<Producto> findProductosNoEnStock();
	
	@Query("SELECT DISTINCT p FROM Producto p WHERE p.sku IN (SELECT b.skuProductoFinal FROM Bom b)")
	List<Producto> findProductosConBom();
	}



