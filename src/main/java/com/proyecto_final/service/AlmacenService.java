package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.Almacen;
import com.proyecto_final.repository.AlmacenRepository;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    public void crearAlmacen(int idCentro, String nombre, int capacidad, String estado) {
        Almacen almacen = new Almacen();
        almacen.setIdCentro(idCentro);
        almacen.setNombre(nombre);
        almacen.setCapacidad(capacidad);
        almacen.setEstado(estado);
        almacenRepository.save(almacen);
    }
    
    public List<Almacen> obtenerTodos() {
        return almacenRepository.findAll();
    }
}