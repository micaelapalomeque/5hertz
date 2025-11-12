package com.proyecto_final.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.ConfigProduccion;

@Repository
public interface ConfigProduccionRepository extends JpaRepository<ConfigProduccion, Integer> {
    
    // Obtener la configuración actual (solo debe haber una fila)
    @Query("SELECT c FROM ConfigProduccion c ORDER BY c.fechaModificacion DESC LIMIT 1")
    Optional<ConfigProduccion> findConfiguracionActual();
}