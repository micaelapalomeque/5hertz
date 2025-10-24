package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    public MovimientoStock(Integer idAlmacen, String sku, Integer cantidad, String tipoMovimiento) {
        this.idAlmacen = idAlmacen;
        this.sku = sku;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = LocalDateTime.now();
    }

}
