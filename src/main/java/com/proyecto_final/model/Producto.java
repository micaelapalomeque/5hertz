package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Producto {

    @Id
    @Column(name = "sku")
    private String sku;

    @Column(name = "id_categoria")
    private int idCategoria;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    public Producto() {}

    public Producto(String sku, int idCategoria, String nombre, String unidadMedida, String descripcion) {
        this.sku = sku;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.descripcion = descripcion;
    }

    public String getSku() { return sku; }
    public int getIdCategoria() { return idCategoria; }
    public String getNombre() { return nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public String getDescripcion() { return descripcion; }
    public void setSku(String sku) { this.sku = sku; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
