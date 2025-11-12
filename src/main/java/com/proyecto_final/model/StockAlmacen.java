package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_almacen")
public class StockAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private int idStock;

    @Column(name = "id_almacen")
    private int idAlmacen;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "cantidad_minima")
    private int cantidadMinima = 0;

    @Column(name = "stock_reservado")
    private int stockReservado = 0;

    @Column(name = "stock_disponible")
    private int stockDisponible = 0;

    @Column(name = "stock_total")
    private int stockTotal = 0;

    public StockAlmacen(int idAlmacen, String sku, int cantidadMinima, int stockReservado, int stockDisponible, int stockTotal) {
        this.idAlmacen = idAlmacen;
        this.sku = sku;
        this.cantidadMinima = cantidadMinima;
        this.stockReservado = stockReservado;
        this.stockDisponible = stockDisponible;
        this.stockTotal = stockTotal;
    }

}
