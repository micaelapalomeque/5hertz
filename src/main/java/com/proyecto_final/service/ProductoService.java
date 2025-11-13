package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    private boolean datosInvalidos(Producto p) {
        if (p == null) return true;
        if (p.getSku() == null || p.getSku().isBlank()) return true;
        if (p.getNombre() == null || p.getNombre().isBlank()) return true;
        if (p.getUnidadMedida() == null || p.getUnidadMedida().isBlank()) return true;
        if (p.getIdCategoria() <= 0) return true;
        return false;
    }

    public boolean altaProducto(Producto producto) {
        if (datosInvalidos(producto)) {
            return false;
        }

        Optional<Producto> existente = productoRepository.findBySku(producto.getSku());
        if (existente.isPresent()) {
            return false;
        }

        productoRepository.save(producto);
        return true;
    }

    public boolean bajaProducto(String sku) {
        if (sku == null || sku.isBlank()) {
            return false;
        }

        Optional<Producto> producto = productoRepository.findBySku(sku);
        if (producto.isEmpty()) {
            return false;
        }

        productoRepository.deleteById(sku);
        return true;
    }

    public Optional<Producto> getBySku(String sku) {
        if (sku == null || sku.isBlank()) {
            return Optional.empty();
        }

        return productoRepository.findById(sku);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosDisponiblesParaStock() {
        return productoRepository.findProductosNoEnStock();
    }
}
