package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sector_template")
public class SectorTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector_template")
    private Integer id;

    @Column(name = "fila")
    private Integer fila;

    @Column(name = "columna")
    private Integer columna;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_producto")
    private String tipoProducto;

    @Column(name = "condicion")
    private String condicion;

    @Column(name = "observaciones")
    private String observaciones;
}
