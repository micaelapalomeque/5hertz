package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "etapa_proceso")
public class EtapaProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etapa")
    private int idEtapa;

    @Column(name = "id_proceso")
    private int idProceso;

    @Column(name = "orden")
    private int orden;

    @Column(name = "nombre_etapa")
    private String nombreEtapa;

    @Column(name = "descripcion")
    private String descripcion;

    public EtapaProceso(int idProceso, int orden, String nombreEtapa, String descripcion) {
        this.idProceso = idProceso;
        this.orden = orden;
        this.nombreEtapa = nombreEtapa;
        this.descripcion = descripcion;
    }
}