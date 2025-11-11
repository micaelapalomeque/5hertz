package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.MaterialPorOp;

@Repository
public interface MaterialPorOpRepository extends JpaRepository<MaterialPorOp, Integer> {

	public List<MaterialPorOp> findByIdOp(int idOp);
	
	public Optional<MaterialPorOp> findByIdOpAndSku(int idOp, String sku);
}
