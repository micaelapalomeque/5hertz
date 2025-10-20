package com.proyecto_final.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_final.model.StockAlmacen;

@Repository
public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Long> {
    List<StockAlmacen> findByIdAlmacen(Long idAlmacen);
}
