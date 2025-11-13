package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.repository.CategoriaProductoRepository;

@Service
public class CategoriaProductoService {

    private final CategoriaProductoRepository repositorio;

    public CategoriaProductoService(CategoriaProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    private boolean datosInvalidos(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) return true;
        if (descripcion == null || descripcion.isBlank()) return true;
        return false;
    }

    public boolean agregarCategoria(String nombre, String descripcion) {
        if (datosInvalidos(nombre, descripcion)) {
            return false;
        }

        Optional<CategoriaProducto> existente = repositorio.findByNombre(nombre);
        if (existente.isPresent()) {
            return false;
        }

        CategoriaProducto nueva = new CategoriaProducto(nombre, descripcion);
        repositorio.save(nueva);
        return true;
    }

    public boolean eliminarCategoria(String nombre) {
        if (nombre == null || nombre.isBlank()) return false;

        Optional<CategoriaProducto> opt = repositorio.findByNombre(nombre);
        if (opt.isEmpty()) return false;

        repositorio.delete(opt.get());
        return true;
    }

    public Optional<CategoriaProducto> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Optional.empty();
        return repositorio.findByNombre(nombre);
    }

    public List<CategoriaProducto> obtenerTodas() {
        return repositorio.findAll();
    }
}
