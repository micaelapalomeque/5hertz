package com.proyecto_final.service;

import com.proyecto_final.model.Usuario;
import com.proyecto_final.repository.UsuarioRepository;


import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder ENCODER;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder ENCODER) {
        this.usuarioRepository = usuarioRepository;
        this.ENCODER = ENCODER;
    }

    public Optional<Usuario> autenticar(String username, String password) {
        return usuarioRepository.findByUsername(username)
            .filter(u -> ENCODER.matches(password, u.getPassword()));
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerActivos() {
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
    
    public String encriptarContraseña(String contraseña) {
    	return ENCODER.encode(contraseña);
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

    public Usuario actualizarUsuario(int idUsuario, Usuario datosActualizados) {
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            usuario.setUsername(datosActualizados.getUsername());
            usuario.setNombre(datosActualizados.getNombre());
            usuario.setRol(datosActualizados.getRol());
            usuario.setEstacionAsignada(datosActualizados.getEstacionAsignada());
            usuario.setEmail(datosActualizados.getEmail());
            if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
                usuario.setPassword(datosActualizados.getPassword());
            }
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public void eliminarUsuario(int idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    public void activarUsuario(int idUsuario) {
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        }
    }
}