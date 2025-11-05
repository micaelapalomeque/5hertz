package com.proyecto_final.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.ReservaMaterial;

@Repository
public interface ReservaMaterialRepository extends JpaRepository<ReservaMaterial, Integer> {

	public List<ReservaMaterial> findByIdOp(int idOp);
	
	public Optional<ReservaMaterial> findByIdOpSku(int idOp, String sku);
}
