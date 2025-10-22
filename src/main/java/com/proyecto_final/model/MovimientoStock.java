package com.proyecto_final.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @Column(name = "id_almacen", nullable = false)
    private Integer idAlmacen;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "tipo_movimiento", length = 20)
    private String tipoMovimiento;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    public MovimientoStock() {}

    public MovimientoStock(Integer idAlmacen, String sku, Integer cantidad, String tipoMovimiento) {
        this.idAlmacen = idAlmacen;
        this.sku = sku;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Integer idMovimiento) { this.idMovimiento = idMovimiento; }

    public Integer getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(Integer idAlmacen) { this.idAlmacen = idAlmacen; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
