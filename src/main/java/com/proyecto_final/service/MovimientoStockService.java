package com.proyecto_final.service;

import org.springframework.stereotype.Service;
import com.proyecto_final.model.MovimientoStock;
import com.proyecto_final.repository.MovimientoStockRepository;

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
}
