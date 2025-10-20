package com.proyecto_final.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_final.model.CategoriaProducto;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
	
    List<CategoriaProducto> findByIdCategoria(Integer idCategoria);
}
