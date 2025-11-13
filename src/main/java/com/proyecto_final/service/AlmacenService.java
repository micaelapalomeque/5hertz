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

    private boolean datosInvalidos(int idCentro, String nombre, int capacidad, String estado) {
        if (idCentro <= 0) return true;
        if (nombre == null || nombre.isBlank()) return true;
        if (estado == null || estado.isBlank()) return true;
        if (capacidad < 0) return true;
        return false;
    }

    public boolean crearAlmacen(int idCentro, String nombre, int capacidad, String estado) {
        if (datosInvalidos(idCentro, nombre, capacidad, estado)) {
            return false;
        }

        Almacen almacen = new Almacen();
        almacen.setIdCentro(idCentro);
        almacen.setNombre(nombre);
        almacen.setCapacidad(capacidad);
        almacen.setEstado(estado);

        almacenRepository.save(almacen);
        return true;
    }

    public List<Almacen> obtenerTodos() {
        return almacenRepository.findAll();
    }
}
