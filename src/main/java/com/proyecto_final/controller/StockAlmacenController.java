package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.service.StockAlmacenService;
import request.HabilitarProductoRequest;
import request.IncrementarStockRequest;

@RestController
@RequestMapping("/stock")
public class StockAlmacenController {

	private StockAlmacenService stockAlmacenService;

	public StockAlmacenController(StockAlmacenService stockAlmacenService) {
		this.stockAlmacenService = stockAlmacenService;
	}
	
	@PostMapping
	public void habilitarProducto(@RequestBody HabilitarProductoRequest request) {
		stockAlmacenService.habilitarProducto(request.getIdAlmacen(), request.getSku());
	}
	
	@PutMapping
	public void incrementarStock(@RequestBody IncrementarStockRequest request) {
		
	}
	
	
}
