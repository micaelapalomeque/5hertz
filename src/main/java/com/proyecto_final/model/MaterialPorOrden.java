package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "material_por_op")
public class MaterialPorOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private int idMaterial;

    @Column(name = "id_op", nullable = false)
    private int idOp;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "cantidad_reservada", nullable = false)
    private int cantidadReservada;

    @Column(name = "cantidad_consumida")
    private int cantidadConsumida = 0;
    
    @Column(name = "cantidad_desperdiciada")
    private int cantidadDesperdiciada = 0;
    
    @Column(name = "cantidad_pendiente")
    private int cantidadPendiente;
    
    @Column(name = "fecha_reserva", insertable = false, updatable = false)
    private java.time.LocalDateTime fechaReserva;

    public MaterialPorOrden(int idOp, String sku, int cantidadReservada) {
        this.idOp = idOp;
        this.sku = sku;
        this.cantidadReservada = cantidadReservada;
        this.cantidadPendiente = cantidadReservada;
    }
}