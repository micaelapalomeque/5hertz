package com.proyecto_final.service;

import com.proyecto_final.dto.SectorConStockDTO;
import com.proyecto_final.model.SectorTemplate;
import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SectorAlmacenService {

    private final SectorTemplateService sectorTemplateService;
    private final StockAlmacenRepository stockAlmacenRepository;

    public SectorAlmacenService(SectorTemplateService sectorTemplateService, 
                              StockAlmacenRepository stockAlmacenRepository) {
        this.sectorTemplateService = sectorTemplateService;
        this.stockAlmacenRepository = stockAlmacenRepository;
    }

    public List<SectorConStockDTO> obtenerSectoresConStock(Integer idAlmacen) {
        // Obtener todos los sectores (plantilla)
        List<SectorTemplate> sectores = sectorTemplateService.obtenerTodos();
        
        // Obtener el stock del almacén agrupado por id_sector_template.
        // Puede haber más de un registro con el mismo idSectorTemplate (distintos movimientos),
        // por lo que usamos una función de merge para evitar IllegalStateException en toMap.
        Map<Integer, StockAlmacen> stockPorSector = stockAlmacenRepository
            .findByIdAlmacen(idAlmacen)
            .stream()
            .filter(stock -> stock.getIdSectorTemplate() != null)
            .collect(Collectors.toMap(
                StockAlmacen::getIdSectorTemplate,
                stock -> stock,
                (existing, replacement) -> {
                    // combinamos sumando los stocks disponibles/totales (son int, no pueden ser null)
                    existing.setStockDisponible(
                        existing.getStockDisponible() + replacement.getStockDisponible()
                    );
                    existing.setStockTotal(
                        existing.getStockTotal() + replacement.getStockTotal()
                    );
                    return existing;
                }
            ));
        
        // Combinar sectores con su stock correspondiente
        return sectores.stream()
            .map(sector -> new SectorConStockDTO(
                sector, 
                stockPorSector.get(sector.getId())
            ))
            .collect(Collectors.toList());
    }
}
