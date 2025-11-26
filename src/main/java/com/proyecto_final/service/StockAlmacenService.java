package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;

@Service
public class StockAlmacenService {

    private final MovimientoStockService movimientoStockService;
    private final StockAlmacenRepository stockAlmacenRepository;

    public StockAlmacenService(
            MovimientoStockService movimientoStockService,
            StockAlmacenRepository stockAlmacenRepository) {

        this.movimientoStockService = movimientoStockService;
        this.stockAlmacenRepository = stockAlmacenRepository;
    }

    private boolean parametrosInvalidos(String sku, int idAlmacen, int cantidad) {
        return sku == null || sku.isBlank() || idAlmacen <= 0 || cantidad <= 0;
    }

    private Optional<StockAlmacen> obtenerRegistro(String sku, int idAlmacen) {
        return stockAlmacenRepository.findBySkuAndIdAlmacen(sku, idAlmacen);
    }

    public StockAlmacen consultarStockProducto(String sku, int idAlmacen) {
        if (sku == null || sku.isBlank() || idAlmacen <= 0) {
            return null;
        }

        return obtenerRegistro(sku, idAlmacen).orElse(null);
    }

    public List<StockAlmacen> consultarStockAlmacen(int idAlmacen) {
        return idAlmacen > 0 ? stockAlmacenRepository.findByIdAlmacen(idAlmacen) : List.of();
    }

    public boolean hayStockDisponible(String sku, int idAlmacen, int cantidad) {
        if (parametrosInvalidos(sku, idAlmacen, cantidad)) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        return opt.isPresent() && opt.get().getStockDisponible() >= cantidad;
    }

    public List<StockAlmacen> listarStock() {
        return stockAlmacenRepository.findAll();
    }

    public boolean modificarCantidadMinima(String sku, int idAlmacen, int cantidadMinima) {
        if (sku == null || sku.isBlank() || idAlmacen <= 0 || cantidadMinima < 0) {
            return false;
        }

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        stock.setCantidadMinima(cantidadMinima);
        stockAlmacenRepository.save(stock);
        return true;
    }

    public boolean asignarSector(String sku, int idAlmacen, Integer idSector) {
        if (sku == null || sku.isBlank() || idAlmacen <= 0) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        stock.setIdSectorTemplate(idSector);
        stockAlmacenRepository.save(stock);
        return true;
    }

    public Integer obtenerSectorParaStock(String sku, int idAlmacen) {
        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        if (opt.isEmpty()) return null;
        return opt.get().getIdSectorTemplate();
    }

    public boolean habilitarProducto(int idAlmacen, String sku) {
        if (sku == null || sku.isBlank() || idAlmacen <= 0) {
            return false;
        }

        Optional<StockAlmacen> existente = stockAlmacenRepository.findBySku(sku);
        if (existente.isPresent()) return false;

        StockAlmacen nuevo = new StockAlmacen(idAlmacen, sku, 0, 0, 0, 0);
        stockAlmacenRepository.save(nuevo);

        return true;
    }

    public boolean ingresarMaterial(String sku, int idAlmacen, int cantidad) {
        if (parametrosInvalidos(sku, idAlmacen, cantidad)) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        stock.setStockDisponible(stock.getStockDisponible() + cantidad);
        stock.setStockTotal(stock.getStockTotal() + cantidad);
        stockAlmacenRepository.save(stock);

        movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "INGRESO");
        return true;
    }

    public boolean retirarMaterial(String sku, int idAlmacen, int cantidad) {
        if (parametrosInvalidos(sku, idAlmacen, cantidad)) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlmacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        if (cantidad > stock.getStockTotal()) return false;

        stock.setStockTotal(stock.getStockTotal() - cantidad);
        stock.setStockDisponible(stock.getStockDisponible() - cantidad);
        stockAlmacenRepository.save(stock);

        movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "RETIRO");
        return true;
    }

    public boolean reservarMaterial(String sku, int idAlacen, int cantidad) {
        if (parametrosInvalidos(sku, idAlacen, cantidad)) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        if (cantidad > stock.getStockDisponible()) return false;

        stock.setStockReservado(stock.getStockReservado() + cantidad);
        stock.setStockDisponible(stock.getStockDisponible() - cantidad);
        stockAlmacenRepository.save(stock);

        movimientoStockService.registrarMovimiento(idAlacen, sku, cantidad, "RESERVA");
        return true;
    }

    public boolean liberarMaterial(String sku, int idAlacen, int cantidad) {
        if (parametrosInvalidos(sku, idAlacen, cantidad)) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        if (cantidad > stock.getStockReservado()) return false;

        stock.setStockReservado(stock.getStockReservado() - cantidad);
        stock.setStockDisponible(stock.getStockDisponible() + cantidad);
        stockAlmacenRepository.save(stock);

        movimientoStockService.registrarMovimiento(idAlacen, sku, cantidad, "LIBERACION");
        return true;
    }

    public boolean consumirMaterial(String sku, int idAlacen, int cantidad, String tipoConsumicion) {
        if (parametrosInvalidos(sku, idAlacen, cantidad)) return false;
        if (tipoConsumicion == null || tipoConsumicion.isBlank()) return false;

        Optional<StockAlmacen> opt = obtenerRegistro(sku, idAlacen);
        if (opt.isEmpty()) return false;

        StockAlmacen stock = opt.get();
        if (cantidad > stock.getStockReservado()) return false;

        stock.setStockTotal(stock.getStockTotal() - cantidad);
        stock.setStockDisponible(stock.getStockDisponible() - cantidad);
        stock.setStockReservado(stock.getStockReservado() - cantidad);
        stockAlmacenRepository.save(stock);

        movimientoStockService.registrarMovimiento(idAlacen, sku, cantidad, tipoConsumicion);
        return true;
    }
public boolean eliminarProducto(String sku, int idAlmacen) {
    Optional<StockAlmacen> stockOpt =
            stockAlmacenRepository.findBySkuAndIdAlmacen(sku, idAlmacen);

    if (stockOpt.isEmpty()) return false;

    stockAlmacenRepository.delete(stockOpt.get());
    return true;
}



}
