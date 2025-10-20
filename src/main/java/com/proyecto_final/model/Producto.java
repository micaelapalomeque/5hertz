package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Producto {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id_producto")
	 private Long idProducto;

	 @Column(name = "id_categoria")
	 private Integer idCategoria;

	 @Column(name = "tipo_producto", length = 50)
	 private String tipoProducto;

	 @Column(name = "sku", length = 50, nullable = false, unique = true)
	 private String sku;

	 @Column(name = "serial_number", length = 100)
	 private String serialNumber;

	 @Column(name = "nombre", length = 150, nullable = false)
	 private String nombre;

	 @Column(name = "descripcion", columnDefinition = "TEXT")
	 private String descripcion;
}
