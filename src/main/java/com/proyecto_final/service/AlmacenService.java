package com.proyecto_final.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;

@Service
public class AlmacenService {

    private StockAlmacenRepository stockAlmacenRepository;

    public AlmacenService(StockAlmacenRepository stockAlmacenRepository) {
    	this.stockAlmacenRepository = stockAlmacenRepository;
    }
    
    public List<StockAlmacen> obtenerStockPorAlmacen(Long idAlmacen) {
        return stockAlmacenRepository.findByIdAlmacen(idAlmacen);
    }
}
