package com.proyecto_final.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.ProductoRepository;

@Service
public class ProductoService {
	
	private ProductoRepository productoRepository;
	
	public ProductoService(ProductoRepository productoRepository) {
	    this.productoRepository = productoRepository;
	}

    public void altaProducto(Producto producto) {
    	productoRepository.save(producto);
    }
    
    public void bajaProducto(String sku) {
    	Optional<Producto> producto = productoRepository.findBySku(sku);
    	if(producto.isPresent()) {
    		productoRepository.deleteById(sku);
    	}
    }
    
    public Optional<Producto> getBySku(String sku) {
    	return productoRepository.findById(sku);
    }

}


