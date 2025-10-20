package com.proyecto_final.service;

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

}
