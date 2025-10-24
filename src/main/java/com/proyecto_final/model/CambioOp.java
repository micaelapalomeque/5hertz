package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cambio_op")
public class CambioOp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cambio")
    private int idCambio;

    @Column(name = "id_op")
    private int idOp;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "fecha_cambio", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCambio;

    @Column(name = "responsable", length = 100)
    private String responsable;
    
    public CambioOp(int idOp, String estado, String responsable) {
    	this.idOp = idOp;
    	this.estado = estado;
    	this.responsable = responsable;
    }
}
