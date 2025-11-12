package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "config_produccion")
public class ConfigProduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cantidad_base_orden", nullable = false)
    private int cantidadBaseOrden = 500;

    @Column(name = "numero_lotes_fijo", nullable = false)
    private int numeroLotesFijo = 10;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @Column(name = "modificado_por")
    private String modificadoPor;

    public ConfigProduccion(int cantidadBaseOrden, int numeroLotesFijo, String modificadoPor) {
        this.cantidadBaseOrden = cantidadBaseOrden;
        this.numeroLotesFijo = numeroLotesFijo;
        this.modificadoPor = modificadoPor;
    }
}