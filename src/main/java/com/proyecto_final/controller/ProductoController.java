package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto_final.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {
	
	private ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}
	
	
	

}
