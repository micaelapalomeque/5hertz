package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrdenProduccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_op")
	private int idOp;
	
	@Column(name = "id_almacen")
	private int idAlmacen;
	
	@Column(name = "sku")
	private String sku;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@Column(name = "estado")
	private String estado = "planificada";
	
	@Column(name = "responsable")
	private String responsable;

	public OrdenProduccion(int idAlmacen, String sku, int cantidad, String estado, String responsable) {
		this.idAlmacen = idAlmacen;
		this.sku = sku;
		this.cantidad = cantidad;
		this.estado = estado;
		this.responsable = responsable;
	}
}
