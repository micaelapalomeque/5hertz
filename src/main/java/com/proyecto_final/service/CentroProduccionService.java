package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.CentroProduccion;
import com.proyecto_final.repository.CentroProduccionRepository;

@Service
public class CentroProduccionService {

    private final CentroProduccionRepository centroProduccionRepository;

    public CentroProduccionService(CentroProduccionRepository centroProduccionRepository) {
        this.centroProduccionRepository = centroProduccionRepository;
    }

    public void crearCentro(String sucursal, String descripcion) {
        CentroProduccion centro = new CentroProduccion();
        centro.setSucursal(sucursal);
        centro.setDescripcion(descripcion);
        centroProduccionRepository.save(centro);
    }
    
    public List<CentroProduccion> obtenerTodos() {
        return centroProduccionRepository.findAll();
    }
}