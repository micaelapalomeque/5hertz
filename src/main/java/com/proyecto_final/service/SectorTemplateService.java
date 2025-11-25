package com.proyecto_final.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.SectorTemplate;
import com.proyecto_final.repository.SectorTemplateRepository;

@Service
public class SectorTemplateService {

    private final SectorTemplateRepository sectorTemplateRepository;

    public SectorTemplateService(SectorTemplateRepository sectorTemplateRepository) {
        this.sectorTemplateRepository = sectorTemplateRepository;
    }

    public List<SectorTemplate> obtenerTodos() {
        return sectorTemplateRepository.findAllByOrderByFilaAscColumnaAsc();
    }

    public SectorTemplate buscarPorId(Integer id) {
        return sectorTemplateRepository.findById(id).orElse(null);
    }
}
