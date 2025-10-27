package com.proyecto_final.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;
import com.proyecto_final.service.MovimientoStockService;

@Service
public class StockAlmacenService {
	private MovimientoStockService movimientoStockService;
	private StockAlmacenRepository stockAlmacenRepository;
	
	public StockAlmacenService(MovimientoStockService movimientoStockService, StockAlmacenRepository stockAlmacenRepository) {
		this.movimientoStockService = movimientoStockService;
		this.stockAlmacenRepository = stockAlmacenRepository;
	}
	
	private boolean estaProductoHabilitado(String sku, int idAlmacen) {
		return stockAlmacenRepository.findBySkuAndIdAlmacen(sku, idAlmacen).isPresent();
	}
	
	private StockAlmacen consultarStockProducto(String sku, int idAlmacen) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			return stockAlmacenRepository.findBySkuAndIdAlmacen(sku, idAlmacen).get();
		}
		return null;
	}
	
	public void habilitarProducto(int idAlmacen, String sku) {
        Optional<StockAlmacen> opt = stockAlmacenRepository.findBySku(sku);

        if (opt.isEmpty()) {
        	 StockAlmacen nuevoStock = new StockAlmacen();
             nuevoStock.setIdAlmacen(idAlmacen);
             nuevoStock.setSku(sku);
             nuevoStock.setCantidadMinima(0);
             nuevoStock.setStockReservado(0);
             nuevoStock.setStockDisponible(0);
             nuevoStock.setStockTotal(0);
             stockAlmacenRepository.save(nuevoStock);
        }
    }
	
	public void incrementarStockTotal(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = consultarStockProducto(sku, idAlmacen);
			registro.setStockDisponible(registro.getStockDisponible() + cantidad);
			registro.setStockTotal(registro.getStockTotal() + cantidad);
			stockAlmacenRepository.save(registro);
			movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "INGRESO");
		}
	}
	
	public void reducirStockTotal(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = consultarStockProducto(sku, idAlmacen);
			if(cantidad <= registro.getStockTotal()) {
				registro.setStockTotal(registro.getStockTotal() - cantidad);
				registro.setStockDisponible(registro.getStockDisponible() - cantidad);
				stockAlmacenRepository.save(registro);
				movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "EGRESO");
			}
		}
	}
	
	public void reservarMaterial(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = consultarStockProducto(sku, idAlmacen);
			if(cantidad <= registro.getStockDisponible()) {
				registro.setStockReservado(registro.getStockReservado() + cantidad);
				registro.setStockDisponible(registro.getStockDisponible() - cantidad);
				stockAlmacenRepository.save(registro);
				movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "RESERVA");
			}
		}
	}
	
	public void liberarMaterial(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = consultarStockProducto(sku, idAlmacen);
			if(cantidad <= registro.getStockReservado()) {
				registro.setStockReservado(registro.getStockReservado() - cantidad);
				registro.setStockDisponible(registro.getStockDisponible() + cantidad);
				stockAlmacenRepository.save(registro);
				movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "LIBERACION");
			}
		}
	}
	
    public void consumirMaterial(String sku, int idAlmacen, int cantidad) {
    	if(estaProductoHabilitado(sku, idAlmacen)) {
    		StockAlmacen registro = consultarStockProducto(sku, idAlmacen);
    		if(cantidad <= registro.getStockReservado()) {
    			registro.setStockTotal(registro.getStockTotal() - cantidad);
    			registro.setStockDisponible(registro.getStockDisponible() - cantidad);
    			registro.setStockReservado(registro.getStockReservado() - cantidad);
    			stockAlmacenRepository.save(registro);
				movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "CONSUMO");
    		}
    	}
    }
	
    public boolean hayStockDisponible(String sku, int idAlmacen, int cantidad) {
    	return consultarStockProducto(sku, idAlmacen).getStockDisponible() >= cantidad;
    }

	
}
