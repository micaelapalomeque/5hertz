package com.proyecto_final.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Bom {
	
	@Id
	@Column(name = "id_bom")
	private int idBom;
	
	@Column(name = "sku_producto_final")
	private String skuProductoFinal;
	
	@Column(name = "sku_material")
	private String skuMaterial;
	
	@Column(name = "cant_por_unidad")
	private int canPorUnidad;

}
