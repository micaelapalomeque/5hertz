package com.proyecto_final.controller;

import com.proyecto_final.dto.SectorConStockDTO;
import com.proyecto_final.service.SectorAlmacenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sectores-almacen")
public class SectorAlmacenController {

    private final SectorAlmacenService sectorAlmacenService;

    public SectorAlmacenController(SectorAlmacenService sectorAlmacenService) {
        this.sectorAlmacenService = sectorAlmacenService;
    }

    @GetMapping("/{idAlmacen}")
    public ResponseEntity<List<SectorConStockDTO>> obtenerSectoresConStock(@PathVariable Integer idAlmacen) {
        List<SectorConStockDTO> sectores = sectorAlmacenService.obtenerSectoresConStock(idAlmacen);
        return ResponseEntity.ok(sectores);
    }
}
