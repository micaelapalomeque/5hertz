// SectorConStockDTO.java
package com.proyecto_final.dto;

import com.proyecto_final.model.SectorTemplate;
import com.proyecto_final.model.StockAlmacen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectorConStockDTO {
    private Integer idSector;
    private Integer fila;
    private Integer columna;
    private String nombre;
    private String tipoProducto;
    private String condicion;
    private String observaciones;
    private String skuProducto;
    private String nombreProducto;
    private Integer stockDisponible;

    public SectorConStockDTO(SectorTemplate sector, StockAlmacen stock) {
        this.idSector = sector.getId();
        this.fila = sector.getFila();
        this.columna = sector.getColumna();
        this.nombre = sector.getNombre();
        this.tipoProducto = sector.getTipoProducto();
        this.condicion = sector.getCondicion();
        this.observaciones = sector.getObservaciones();
        
        if (stock != null) {
            this.skuProducto = stock.getSku();
            this.stockDisponible = stock.getStockDisponible();
            // El nombre del producto se manejará desde el frontend
            this.nombreProducto = null;
        }
    }
}