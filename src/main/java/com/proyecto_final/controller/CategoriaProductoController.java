package com.proyecto_final.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.CategoriaProducto;
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
	
	@DeleteMapping("/{nombre}")
	public void eliminarCategoria(@PathVariable String nombre)
	{
		  servicio.eliminarCategoria(nombre);
	}
	
	@GetMapping
	public List<CategoriaProducto> obtenerTodas() {
		return servicio.obtenerTodas();
	}
	
}
