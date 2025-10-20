package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.ProductoRepository;

@Service
public class ProductoService {
	
	private ProductoRepository productoRepository;
	
	public ProductoService(ProductoRepository productoRepository) {
	    this.productoRepository = productoRepository;
	}
	
	public List<Producto> findByTipoProducto(String tipoProducto) {

        return productoRepository.findByTipoProducto(tipoProducto);
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}


