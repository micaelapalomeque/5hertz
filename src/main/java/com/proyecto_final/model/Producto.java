package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
