package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;
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
    
}

