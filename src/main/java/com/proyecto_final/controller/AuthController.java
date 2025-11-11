package com.proyecto_final.controller;

import com.proyecto_final.model.Usuario;
import com.proyecto_final.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Usuario> usuario = usuarioService.autenticar(username, password);

        if (usuario.isPresent() && usuario.get().isActivo()) {
            Usuario user = usuario.get();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "user", Map.of(
                    "id", user.getIdUsuario(),
                    "username", user.getUsername(),
                    "nombre", user.getNombre(),
                    "rol", user.getRol(),
                    "estacion_asignada", user.getEstacionAsignada() != null ? user.getEstacionAsignada() : ""
                )
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Credenciales inválidas"
            ));
        }
    }
}