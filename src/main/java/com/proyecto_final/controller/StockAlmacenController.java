package com.proyecto_final.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.service.StockAlmacenService;
import request.HabilitarProductoRequest;
import request.ModificarStockRequest;
import com.proyecto_final.model.StockAlmacen;

@RestController
@RequestMapping("/stock")
public class StockAlmacenController {

	private StockAlmacenService stockAlmacenService;

	public StockAlmacenController(StockAlmacenService stockAlmacenService) {
		this.stockAlmacenService = stockAlmacenService;
	}
	
	@PostMapping("/habilitar-producto")
	public void habilitarProducto(@RequestBody HabilitarProductoRequest request) {
		stockAlmacenService.habilitarProducto(request.getIdAlmacen(), request.getSku());
	}
	
	@PutMapping("/incrementar")
	public void incrementarStock(@RequestBody ModificarStockRequest request) {
		stockAlmacenService.incrementarStock(request.getSku(), request.getIdAlmacen(), request.getCantidad());
	}
	
	@PutMapping("/reducir")
	public void reducirStock(@RequestBody ModificarStockRequest request) {
		stockAlmacenService.reducirStock(request.getSku(), request.getIdAlmacen(), request.getCantidad());
	}
	
	@GetMapping("/consultar-stock/{idAlmacen}")
	public Optional<StockAlmacen> consultarStockTotal(@PathVariable int idAlmacen) {
		return stockAlmacenService.consultarStock(idAlmacen);
	}
	
	
}
