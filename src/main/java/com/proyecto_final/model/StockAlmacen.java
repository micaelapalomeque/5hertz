package com.proyecto_final.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_almacen")
public class StockAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;

    @Column(name = "id_almacen")
    private Long idAlmacen;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "cantidad", precision = 12, scale = 2, nullable = false)
    private BigDecimal cantidad;

    @Column(name = "cantidad_minima", precision = 12, scale = 2, nullable = false)
    private BigDecimal cantidadMinima;
    
    public Long getIdStock() { return idStock; }
    public void setIdStock(Long idStock) { this.idStock = idStock; }

    public long getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(long idAlmacen) { this.idAlmacen = idAlmacen; }

    public long getIdProducto() { return idProducto; }
    public void setIdProducto(long idProducto) { this.idProducto = idProducto; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getCantidadMinima() { return cantidadMinima; }
    public void setCantidadMinima(BigDecimal cantidadMinima) { this.cantidadMinima = cantidadMinima; }

}



