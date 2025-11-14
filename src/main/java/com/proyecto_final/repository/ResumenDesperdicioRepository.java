package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.ResumenDesperdicio;

@Repository
public interface ResumenDesperdicioRepository extends JpaRepository<ResumenDesperdicio, Integer> {
    
    Optional<ResumenDesperdicio> findByIdOp(int idOp);
    
    @Query("SELECT r FROM ResumenDesperdicio r ORDER BY r.gramosDesperdiciados DESC")
    List<ResumenDesperdicio> findTop3ByOrderByGramosDesc();
}