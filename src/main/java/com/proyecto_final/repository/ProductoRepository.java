package com.proyecto_final.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
	Optional<Producto> findBySku(String sku);	
	}



