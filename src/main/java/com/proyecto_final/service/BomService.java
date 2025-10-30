package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto_final.model.Bom;
import com.proyecto_final.repository.BomRepository;

@Service
public class BomService {

    private final BomRepository bomRepository;

    public BomService(BomRepository bomRepository) {
        this.bomRepository = bomRepository;
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
    
}

