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
	
	private StockAlmacen getStockProducto(String sku, int idAlmacen) {
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
	
	///////////////REVISAR////////////////////////////////////////////////////////////////
	public void incrementarStock(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = getStockProducto(sku, idAlmacen);
			registro.setStockDisponible(registro.getStockDisponible() + cantidad);
			registro.setStockTotal(registro.getStockTotal() + cantidad);
			stockAlmacenRepository.save(registro);
			movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "INGRESO");
		}
	}
	
	public void reducirStockTotal(String sku, int idAlmacen, int cantidad) {
		if(estaProductoHabilitado(sku, idAlmacen)) {
			StockAlmacen registro = getStockProducto(sku, idAlmacen);
			if(cantidad < registro.getStockTotal()) {
				registro.setStockDisponible(registro.getStockDisponible() - cantidad);
				registro.setStockTotal(registro.getStockTotal() - cantidad);
				stockAlmacenRepository.save(registro);
				movimientoStockService.registrarMovimiento(idAlmacen, sku, cantidad, "EGRESO");
			}
		}
	}
	
	public void reducirStockReservado(String sku, int idAlmacen, int cantidad) {
    
	}

    public void consumirMaterial(String sku, int idAlmacen, int cantidad) {
    	
    }
	
	public Optional<StockAlmacen> consultarStock(int idAlmacen) {
		return stockAlmacenRepository.findByIdAlmacen(idAlmacen);
	}
	
	public void reservarStock(int idAlmacen, String sku, int cantidad) {
		
	}
	
	public void ingresoManual() {
	
	}
	
	public void egresoManual() {
		
	}
	
	///////////////REVISAR////////////////////////////////////////////////////////////////

	
	
	
	
}
