package com.proyecto_final.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.repository.CategoriaProductoRepository;
import com.proyecto_final.model.CategoriaProducto;

@Service
public class CategoriaProductoService {
	
	private CategoriaProductoRepository repositorio;
	
	public CategoriaProductoService(CategoriaProductoRepository repositorio) 
	{
		this.repositorio = repositorio;
	}
	
	public void agregarCategoria(String nombre, String descripcion) {
		CategoriaProducto categoriaProducto = new CategoriaProducto(nombre, descripcion);
		repositorio.save(categoriaProducto);
	}
	
	public void eliminarCategoria(String nombre) {
	    Optional<CategoriaProducto> opt = repositorio.findByNombre(nombre);   
	    if (opt.isPresent()) {
	        CategoriaProducto categoria = opt.get();
	        repositorio.delete(categoria);
	    }
	}
	
	public Optional<CategoriaProducto> findByNombre(String nombre) {
		return repositorio.findByNombre(nombre);
	}
}

