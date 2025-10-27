package com.proyecto_final.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.proyecto_final.repository.BomRepository;
import com.proyecto_final.repository.ProductoRepository;
import com.proyecto_final.model.Bom;
import com.proyecto_final.model.Producto;
import java.util.List;

@Service
public class EstimacionService {
	
	private BomRepository bomRepository;
	private ProductoRepository productoRepository;
	
	public EstimacionService(BomRepository bomRepository) {
		this.bomRepository = bomRepository;
	}
	
	//Crear clase response que contenga lista de sku : cantidad, y un tiempo total;

}
