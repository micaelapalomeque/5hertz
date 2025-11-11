package com.proyecto_final.repository;

import com.proyecto_final.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    
    List<Usuario> findByRol(String rol);
    
    List<Usuario> findByEstacionAsignada(String estacionAsignada);
    
    List<Usuario> findByActivoTrue();
}