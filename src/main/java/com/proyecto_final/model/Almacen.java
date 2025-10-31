package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private int idAlmacen;

    @Column(name = "id_centro")
    private int idCentro;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "capacidad")
    private int capacidad;

    @Column(name = "estado")
    private String estado;
}