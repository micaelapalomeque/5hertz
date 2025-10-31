package com.proyecto_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.Almacen;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
}