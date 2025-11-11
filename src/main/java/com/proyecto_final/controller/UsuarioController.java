package com.proyecto_final.controller;

import com.proyecto_final.model.Usuario;
import com.proyecto_final.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/rol/{rol}")
    public List<Usuario> obtenerPorRol(@PathVariable String rol) {
        return usuarioService.obtenerPorRol(rol);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @PutMapping("/{id}/estacion")
    public ResponseEntity<?> actualizarEstacion(@PathVariable int id, 
                                              @RequestBody Map<String, String> request) {
        usuarioService.actualizarEstacionAsignada(id, request.get("estacion"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}