package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.StockAlmacen;

@Repository
public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Integer> {

    Optional<StockAlmacen> findBySku(String sku);

    List<StockAlmacen> findByIdAlmacen(int idAlmacen);

    Optional<StockAlmacen> findBySkuAndIdAlmacen(String sku, int idAlmacen);
}
