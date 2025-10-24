package com.proyecto_final.service;

import org.springframework.stereotype.Service;
import com.proyecto_final.model.MovimientoStock;
import com.proyecto_final.repository.MovimientoStockRepository;
import com.proyecto_final.model.MovimientoStock;
import java.util.List;

@Service
public class MovimientoStockService {
	
	private MovimientoStockRepository movimientoStockRepository;

	public MovimientoStockService(MovimientoStockRepository movimientoStockRepository) {
		this.movimientoStockRepository = movimientoStockRepository;
	}
	
	public void registrarMovimiento(int idAlmacen, String sku, int cantidad, String tipoMovimiento) {
		 MovimientoStock movimiento = new MovimientoStock(idAlmacen, sku, cantidad, tipoMovimiento);
	     movimientoStockRepository.save(movimiento);
	}
	
	public List<MovimientoStock> consultarEgresos() {
		return movimientoStockRepository.findByTipoMovimiento("Egreso");
	}
	
	public List<MovimientoStock> consultarIngresos(){
		return movimientoStockRepository.findByTipoMovimiento("Ingreso");
	}
	
	public List<MovimientoStock> consultarPorSku(String sku) {
		return movimientoStockRepository.findBySku(sku);
	}
	
	public List<MovimientoStock> consultarTodos() {
		return movimientoStockRepository.findAll();
	}
	
	
}
