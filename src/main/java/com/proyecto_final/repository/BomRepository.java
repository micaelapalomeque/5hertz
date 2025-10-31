package com.proyecto_final.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.Bom;

@Repository
public interface BomRepository extends JpaRepository<Bom, Integer>{

	List<Bom> findBySkuProductoFinal(String skuProductoFinal);
	
	void deleteBySkuProductoFinal(String skuProductoFinal);
	
}
