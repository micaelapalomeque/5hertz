package com.proyecto_final.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resumen_desperdicio")
public class ResumenDesperdicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "id_op")
    private int idOp;
    
    @Column(name = "motivo_principal")
    private String motivoPrincipal;
    
    @Column(name = "sku_mayor_desperdicio")
    private String skuMayorDesperdicio;
    
    @Column(name = "gramos_desperdiciados")
    private int gramosDesperdiciados;

    public ResumenDesperdicio() {}

    public ResumenDesperdicio(int idOp, String motivoPrincipal, String skuMayorDesperdicio, int gramosDesperdiciados) {
        this.idOp = idOp;
        this.motivoPrincipal = motivoPrincipal;
        this.skuMayorDesperdicio = skuMayorDesperdicio;
        this.gramosDesperdiciados = gramosDesperdiciados;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdOp() { return idOp; }
    public void setIdOp(int idOp) { this.idOp = idOp; }
    
    public String getMotivoPrincipal() { return motivoPrincipal; }
    public void setMotivoPrincipal(String motivoPrincipal) { this.motivoPrincipal = motivoPrincipal; }
    
    public String getSkuMayorDesperdicio() { return skuMayorDesperdicio; }
    public void setSkuMayorDesperdicio(String skuMayorDesperdicio) { this.skuMayorDesperdicio = skuMayorDesperdicio; }
    
    public int getGramosDesperdiciados() { return gramosDesperdiciados; }
    public void setGramosDesperdiciados(int gramosDesperdiciados) { this.gramosDesperdiciados = gramosDesperdiciados; }
}