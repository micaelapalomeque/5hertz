package com.proyecto_final.service;

import java.util.List;
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

    public List<Bom> obtenerListaMateriales(String skuProductoFinal) {
        return bomRepository.findBySkuProductoFinal(skuProductoFinal);
    }
    
    public void crearBom(String skuProductoFinal, String skuMaterial, int cantPorUnidad) {
        Bom bom = new Bom();
        bom.setSkuProductoFinal(skuProductoFinal);
        bom.setSkuMaterial(skuMaterial);
        bom.setCanPorUnidad(cantPorUnidad);
        bomRepository.save(bom);
    }
    
    @Transactional
    public void eliminarBomProducto(String skuProductoFinal) {
        bomRepository.deleteBySkuProductoFinal(skuProductoFinal);
    }
    
    public List<Producto> obtenerProductosFabricables() {
        return productoRepository.findProductosConBom();
    }
    
}

