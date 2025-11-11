package com.proyecto_final.service;

import org.springframework.stereotype.Service;

@Service
public class ProduccionService {

	private StockAlmacenService stockAlmacenService;
	private MovimientoStockService movimientoStockService;
	private MaterialPorOpService reservaMaterialService;
	
	public ProduccionService(StockAlmacenService stockAlmacenService,
			MovimientoStockService movimientoStockService, MaterialPorOpService reservaMaterialService) {
		
	}
	
	public void registrarConsumo(int idOp, String sku, int cantidad) {
		//descontarlo del stock generando el movimiento de tipo consumo
		//registrarlo en reserva material como +1 producido y -1 reservado y -1 pendiente
	}
	
	public void registrarDesperdicio(int idOp, String sku, int cantidad) {
		
	}
}
