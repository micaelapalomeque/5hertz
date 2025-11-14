package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "registro_desperdicio")
public class RegistroDesperdicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private int idRegistro;

    @Column(name = "id_op", nullable = false)
    private int idOp;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "cantidad_desperdiciada", nullable = false)
    private int cantidadDesperdiciada;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estacion")
    private String estacion;

    @Column(name = "operario")
    private String operario;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public RegistroDesperdicio(int idOp, String sku, int cantidadDesperdiciada, String motivo, String observaciones, String estacion, String operario) {
        this.idOp = idOp;
        this.sku = sku;
        this.cantidadDesperdiciada = cantidadDesperdiciada;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.estacion = estacion;
        this.operario = operario;
    }
}