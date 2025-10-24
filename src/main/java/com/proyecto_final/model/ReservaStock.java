package com.proyecto_final.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva_stock")
public class ReservaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private int idReserva;

    @Column(name = "id_op")
    private int idOp;

    @Column(name = "id_almacen")
    private int idAlmacen;

    @Column(name = "sku", length = 50)
    private String sku;

    @Column(name = "cantidad_reservada", nullable = false)
    private int cantidadReservada;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "fecha_reserva", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaReserva;
}
