package com.proyecto_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.CambioOp;
import java.util.List;

@Repository
public interface CambioOpRepository extends JpaRepository<CambioOp, Integer>{

	List<CambioOp> findByIdOpOrderByFechaCambioDesc(int idOp);

	
}
