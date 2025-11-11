package com.proyecto_final.service;

import com.proyecto_final.model.Usuario;
import com.proyecto_final.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> autenticar(String username, String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findByActivoTrue();
    }

    public List<Usuario> obtenerPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void actualizarEstacionAsignada(int idUsuario, String nuevaEstacion) {
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            usuario.setEstacionAsignada(nuevaEstacion);
            usuarioRepository.save(usuario);
        }
    }

    public void desactivarUsuario(int idUsuario) {
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }
    }
}