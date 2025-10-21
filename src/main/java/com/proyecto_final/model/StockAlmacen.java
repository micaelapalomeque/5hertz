package com.proyecto_final.model;

import jakarta.persistence.*;

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

    public StockAlmacen() {}

    public StockAlmacen(int idAlmacen, String sku, int cantidadMinima, int stockReservado,
                        int stockDisponible, int stockTotal) {
        this.idAlmacen = idAlmacen;
        this.sku = sku;
        this.cantidadMinima = cantidadMinima;
        this.stockReservado = stockReservado;
        this.stockDisponible = stockDisponible;
        this.stockTotal = stockTotal;
    }

    public int getIdStock() { return idStock; }
    public void setIdStock(int idStock) { this.idStock = idStock; }

    public int getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(int idAlmacen) { this.idAlmacen = idAlmacen; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getCantidadMinima() { return cantidadMinima; }
    public void setCantidadMinima(int cantidadMinima) { this.cantidadMinima = cantidadMinima; }

    public int getStockReservado() { return stockReservado; }
    public void setStockReservado(int stockReservado) { this.stockReservado = stockReservado; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }
}
