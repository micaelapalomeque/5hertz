package com.proyecto_final.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_final.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	Producto findBySku(String sku);
	
    java.util.List<Producto> findByTipoProducto(String tipoProducto);

    java.util.List<Producto> findByIdCategoria(Integer idCategoria);
}


