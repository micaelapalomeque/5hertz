package com.proyecto_final.controller;

import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.model.Producto;
import com.proyecto_final.service.CategoriaProductoService;
import com.proyecto_final.service.ProductoService;
import request.ProductoRequest;

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
	public void altaProducto(@RequestBody ProductoRequest request) {
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
	
	@DeleteMapping("/{sku}")
	public void bajaProducto(@PathVariable String sku) {
		productoService.bajaProducto(sku);
	}
	
	@GetMapping("/{sku}")
	public Producto consultarPorSku(@PathVariable String sku) {
	    Optional<Producto> opt = productoService.getBySku(sku);
	    if (opt.isPresent()) {
	        return opt.get();
	    } else {
	        return null;
	    }
	}
	
	
}
