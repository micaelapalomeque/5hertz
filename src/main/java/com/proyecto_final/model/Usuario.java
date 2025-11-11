package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "activo")
    private boolean activo = true;

    @Column(name = "estacion_asignada")
    private String estacionAsignada;

    @Column(name = "email")
    private String email;

    public Usuario(String username, String password, String nombre, String rol, String estacionAsignada, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.estacionAsignada = estacionAsignada;
        this.email = email;
        this.activo = true;
    }
}