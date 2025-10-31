package com.proyecto_final.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.service.StockAlmacenService;

import request.ConsultarStockRequest;
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
	
	@PutMapping("/habilitar-producto")
	public void habilitarProducto(@RequestBody HabilitarProductoRequest request) {
		stockAlmacenService.habilitarProducto(request.getIdAlmacen(), request.getSku());
	}
	
	@PutMapping("/incrementar")
	public void incrementarStock(@RequestBody ModificarStockRequest request) {
		stockAlmacenService.incrementarStockTotal(request.getSku(), request.getIdAlmacen(), request.getCantidad());
	}
	
	@PutMapping("/reducir")
	public void reducirStock(@RequestBody ModificarStockRequest request) {
		stockAlmacenService.reducirStockTotal(request.getSku(), request.getIdAlmacen(), request.getCantidad());
	}
	
	@GetMapping("/consultar-almacen")
	public List<StockAlmacen> consultarStockAlmacen(@PathVariable int idStock) {
		return stockAlmacenService.consultarStockAlmacen(idStock);
	}
	
	@GetMapping("/consultar-producto-almacen")
	public StockAlmacen consultarStockProducto(
	        @RequestParam String sku,
	        @RequestParam int idAlmacen) {
	    return stockAlmacenService.consultarStockProducto(sku, idAlmacen);
	}

	@GetMapping("/todos")
	public List<StockAlmacen> obtenerTodosLosStocks() {
		return stockAlmacenService.obtenerTodosLosStocks();
	}

	@PutMapping("/actualizar-minimo")
	public void actualizarCantidadMinima(@RequestBody ModificarStockRequest request) {
		stockAlmacenService.actualizarCantidadMinima(request.getSku(), request.getIdAlmacen(), request.getCantidad());
	}
}
