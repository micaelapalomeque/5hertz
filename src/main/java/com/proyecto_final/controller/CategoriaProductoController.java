package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.service.CategoriaProductoService;

import request.AgregarCategoriaRequest;

@RestController
@RequestMapping("/categorias")
public class CategoriaProductoController {
	
	private CategoriaProductoService servicio;

	public CategoriaProductoController(CategoriaProductoService servicio) {
		this.servicio = servicio;
	}
	
	@PostMapping
	public void agregarCategoria(@RequestBody AgregarCategoriaRequest objeto) {
		servicio.agregarCategoria(objeto.getNombre(), objeto.getDescripcion());
	}
	
	
}
