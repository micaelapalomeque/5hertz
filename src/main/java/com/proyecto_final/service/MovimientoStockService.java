package com.proyecto_final.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto_final.model.MovimientoStock;
import com.proyecto_final.repository.MovimientoStockRepository;

@Service
public class MovimientoStockService {

    private final MovimientoStockRepository movimientoStockRepository;

    public MovimientoStockService(MovimientoStockRepository movimientoStockRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
    }

    private boolean datosInvalidos(int idAlmacen, String sku, int cantidad, String tipoMovimiento) {
        if (idAlmacen <= 0) return true;
        if (sku == null || sku.isBlank()) return true;
        if (cantidad <= 0) return true;
        if (tipoMovimiento == null || tipoMovimiento.isBlank()) return true;
        return false;
    }

    public boolean registrarMovimiento(int idAlmacen, String sku, int cantidad, String tipoMovimiento) {

        if (datosInvalidos(idAlmacen, sku, cantidad, tipoMovimiento)) {
            return false;
        }

        String tipo = tipoMovimiento.trim().toUpperCase();

        MovimientoStock movimiento = new MovimientoStock(
                idAlmacen,
                sku,
                cantidad,
                tipo
        );

        movimientoStockRepository.save(movimiento);

        return true;
    }

    public List<MovimientoStock> consultarEgresos() {
        return movimientoStockRepository.findByTipoMovimiento("EGRESO");
    }

    public List<MovimientoStock> consultarIngresos() {
        return movimientoStockRepository.findByTipoMovimiento("INGRESO");
    }

    public List<MovimientoStock> consultarPorSku(String sku) {
        if (sku == null || sku.isBlank()) {
            return List.of();
        }
        return movimientoStockRepository.findBySku(sku);
    }

    public List<MovimientoStock> consultarTodos() {
        return movimientoStockRepository.findAll();
    }
}
