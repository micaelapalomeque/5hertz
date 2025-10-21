package com.proyecto_final.controller;

import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.model.Producto;
import com.proyecto_final.service.CategoriaProductoService;
import com.proyecto_final.service.ProductoService;
import request.RequestProducto;

@RestController
@RequestMapping("/productos")
public class ProductoController {
	
	private ProductoService productoService;
	private CategoriaProductoService categoriaProductoService;

	public ProductoController(ProductoService productoService, CategoriaProductoService categoriaProductoService) {
		this.productoService = productoService;
		this.categoriaProductoService = categoriaProductoService;
	}
	
	@PostMapping
	public void altaProducto(@RequestBody RequestProducto request) {
	    Optional<CategoriaProducto> opt = categoriaProductoService.findByNombre(request.getNombreCategoria());
	    if (opt.isPresent()) {
	        CategoriaProducto cat = opt.get();
	        Producto producto = new Producto(
	            request.getSku(),
	            cat.getIdCategoria(),
	            request.getNombre(),
	            request.getUnidadMedida(),
	            request.getDescripcion()
	        );
	        productoService.altaProducto(producto);
	    } 
	}

	

}
