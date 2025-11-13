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

    @Column(name = "id_op")
    private Integer idOp;

    @Column(name = "operario_responsable")
    private Integer operarioResponsable;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "cantidad_total_unidades")
    private Integer cantidadTotalUnidades;

    @Column(name = "cantidad_procesada_unidades")
    private Integer cantidadProcesadaUnidades = 0;

    @Column(name = "cantidad_pendiente_unidades")
    private Integer cantidadPendienteUnidades;

    @Column(name = "lotes_completados")
    private Integer lotesCompletados = 0;

    @Column(name = "estado")
    private String estado = "PENDIENTE";

    public EtapaProceso(int idProceso, int orden, String nombreEtapa, String descripcion) {
        this.idProceso = idProceso;
        this.orden = orden;
        this.nombreEtapa = nombreEtapa;
        this.descripcion = descripcion;
    }
}