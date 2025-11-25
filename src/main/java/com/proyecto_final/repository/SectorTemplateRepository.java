package com.proyecto_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto_final.model.SectorTemplate;
import java.util.List;

@Repository
public interface SectorTemplateRepository extends JpaRepository<SectorTemplate, Integer> {
    List<SectorTemplate> findAllByOrderByFilaAscColumnaAsc();
}
