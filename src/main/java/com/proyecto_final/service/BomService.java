package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto_final.model.Bom;
import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.BomRepository;
import com.proyecto_final.repository.ProductoRepository;

@Service
public class BomService {

    private final BomRepository bomRepository;
    private final ProductoRepository productoRepository;

    public BomService(BomRepository bomRepository, ProductoRepository productoRepository) {
        this.bomRepository = bomRepository;
        this.productoRepository = productoRepository;
    }

    private boolean datosInvalidos(String skuProductoFinal, String skuMaterial, int cantPorUnidad) {
        if (skuProductoFinal == null || skuProductoFinal.isBlank()) return true;
        if (skuMaterial == null || skuMaterial.isBlank()) return true;
        if (cantPorUnidad <= 0) return true;
        return false;
    }

    public List<Bom> obtenerListaMateriales(String skuProductoFinal) {
        if (skuProductoFinal == null || skuProductoFinal.isBlank()) {
            return List.of();
        }
        return bomRepository.findBySkuProductoFinal(skuProductoFinal);
    }

    public boolean crearBom(String skuProductoFinal, String skuMaterial, int cantPorUnidad) {
        if (datosInvalidos(skuProductoFinal, skuMaterial, cantPorUnidad)) {
            return false;
        }

        Optional<Producto> finalProducto = productoRepository.findBySku(skuProductoFinal);
        if (finalProducto.isEmpty()) {
            return false;
        }

        Optional<Producto> materialProducto = productoRepository.findBySku(skuMaterial);
        if (materialProducto.isEmpty()) {
            return false;
        }

        Bom bom = new Bom();
        bom.setSkuProductoFinal(skuProductoFinal);
        bom.setSkuMaterial(skuMaterial);
        bom.setCanPorUnidad(cantPorUnidad);

        bomRepository.save(bom);
        return true;
    }

    @Transactional
    public boolean eliminarBomProducto(String skuProductoFinal) {
        if (skuProductoFinal == null || skuProductoFinal.isBlank()) {
            return false;
        }

        List<Bom> lista = bomRepository.findBySkuProductoFinal(skuProductoFinal);
        if (lista.isEmpty()) {
            return false;
        }

        bomRepository.deleteBySkuProductoFinal(skuProductoFinal);
        return true;
    }

    public List<Producto> obtenerProductosFabricables() {
        return productoRepository.findProductosConBom();
    }
}
