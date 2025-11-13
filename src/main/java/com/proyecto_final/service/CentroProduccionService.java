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

    private boolean datosInvalidos(String sucursal, String descripcion) {
        if (sucursal == null || sucursal.isBlank()) return true;
        if (descripcion == null || descripcion.isBlank()) return true;
        return false;
    }

    public boolean crearCentro(String sucursal, String descripcion) {
        if (datosInvalidos(sucursal, descripcion)) {
            return false;
        }

        CentroProduccion centro = new CentroProduccion();
        centro.setSucursal(sucursal);
        centro.setDescripcion(descripcion);

        centroProduccionRepository.save(centro);
        return true;
    }

    public List<CentroProduccion> obtenerTodos() {
        return centroProduccionRepository.findAll();
    }
}
