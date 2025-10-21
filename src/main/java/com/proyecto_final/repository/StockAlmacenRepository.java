package com.proyecto_final.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.StockAlmacen;

@Repository
public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Integer> {

    // Buscar un registro por el SKU del producto
    Optional<StockAlmacen> findBySku(String sku);

    // Buscar por ID del almacén
    Optional<StockAlmacen> findByIdAlmacen(int idAlmacen);

    // Si quisieras buscar por ambos (por ejemplo, para un producto en un almacén específico)
    Optional<StockAlmacen> findBySkuAndIdAlmacen(String sku, int idAlmacen);
}
