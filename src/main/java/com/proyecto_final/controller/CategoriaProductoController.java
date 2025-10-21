package com.proyecto_final.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@DeleteMapping ("/{id}")
	public ResponseEntity<String> eliminarCategoria(@PathVariable int id)
	{
		  boolean eliminada = servicio.eliminarCategoria(id);
		    if (eliminada) 
		    {
		        return ResponseEntity.ok("Categoría eliminada correctamente");
		    } 
		    else 
		    {
		        return ResponseEntity.status(404).body("Categoría no encontrada");
		    }
	}
	
}
