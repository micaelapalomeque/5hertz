package com.proyecto_final.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;

@RestController
@RequestMapping("/stock")
public class AlmacenController {

    private final StockAlmacenRepository stockAlmacenRepository;

    public AlmacenController(StockAlmacenRepository stockAlmacenRepository) {
        this.stockAlmacenRepository = stockAlmacenRepository;
    }

    @GetMapping("/{idAlmacen}")
    public List<StockAlmacen> consultarStockPorAlmacen(@PathVariable Long idAlmacen) {
        return stockAlmacenRepository.findByIdAlmacen(idAlmacen);
    }
}