package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lote_proceso")
public class LoteProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private int idLote;

    @Column(name = "id_etapa", nullable = false)
    private int idEtapa;
    
    @Column(name = "id_op", nullable = false)
    private int idOp;

    @Column(name = "unidades_lote", nullable = false)
    private int unidadesLote;
    
    @Column(name = "estacion_actual", nullable = false)
    private String estacionActual = "LAVADO";

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "operario_responsable")
    private Integer operarioResponsable;

    @Column(name = "estado")
    private String estado = "EN_PROCESO"; // EN_PROCESO, COMPLETADO

    public LoteProceso(int idEtapa, int idOp, int unidadesLote) {
        this.idEtapa = idEtapa;
        this.idOp = idOp;
        this.unidadesLote = unidadesLote;
        this.estacionActual = "LAVADO";
    }
}