package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.MaterialPorOrden;
import com.proyecto_final.repository.MaterialPorOrdenRepository;

@Service
public class MaterialPorOrdenService {

    private final MaterialPorOrdenRepository materialPorOrdenRepository;

    public MaterialPorOrdenService(MaterialPorOrdenRepository materialPorOrdenRepository) {
        this.materialPorOrdenRepository = materialPorOrdenRepository;
    }

    public void crearRegistroInicial(int idOp, String sku, int cantidadReservada) {
        MaterialPorOrden material = new MaterialPorOrden(idOp, sku, cantidadReservada);
        materialPorOrdenRepository.save(material);
    }

    public boolean registrarDesperdicio(int idOp, String sku, int cantidadDesperdiciada) {
        Optional<MaterialPorOrden> opt = materialPorOrdenRepository.findByIdOpAndSku(idOp, sku);
        if (opt.isEmpty()) {
            return false;
        }
        
        MaterialPorOrden material = opt.get();
        int nuevoDesperdicio = material.getCantidadDesperdiciada() + cantidadDesperdiciada;
        
        // Validar que el desperdicio no supere la cantidad reservada
        if (nuevoDesperdicio > material.getCantidadReservada()) {
            return false; // No se puede desperdiciar más de lo reservado
        }
        
        // Actualizar desperdicio
        material.setCantidadDesperdiciada(nuevoDesperdicio);
        
        // Recalcular cantidad pendiente: reservada - consumida - desperdiciada
        int nuevaPendiente = material.getCantidadReservada() - material.getCantidadConsumida() - nuevoDesperdicio;
        material.setCantidadPendiente(Math.max(nuevaPendiente, 0));
        
        materialPorOrdenRepository.save(material);
        return true;
    }

    public List<MaterialPorOrden> obtenerMaterialesPorOrden(int idOp) {
        return materialPorOrdenRepository.findByIdOp(idOp);
    }
    
    public boolean consumirMaterial(int idOp, String sku, int cantidadConsumida) {
        Optional<MaterialPorOrden> opt = materialPorOrdenRepository.findByIdOpAndSku(idOp, sku);
        if (opt.isEmpty()) {
            return false;
        }
        
        MaterialPorOrden material = opt.get();
        int nuevoConsumido = material.getCantidadConsumida() + cantidadConsumida;
        
        // Validar que no se consuma más de lo disponible (reservado - desperdiciado)
        int disponible = material.getCantidadReservada() - material.getCantidadDesperdiciada();
        if (nuevoConsumido > disponible) {
            return false;
        }
        
        // Actualizar consumido
        material.setCantidadConsumida(nuevoConsumido);
        
        // Recalcular cantidad pendiente
        int nuevaPendiente = material.getCantidadReservada() - nuevoConsumido - material.getCantidadDesperdiciada();
        material.setCantidadPendiente(Math.max(nuevaPendiente, 0));
        
        materialPorOrdenRepository.save(material);
        return true;
    }
    
    public void liberarMaterialesPorOrden(int idOp) {
        List<MaterialPorOrden> materiales = materialPorOrdenRepository.findByIdOp(idOp);
        // Eliminar todos los registros de esta orden
        materialPorOrdenRepository.deleteAll(materiales);
    }
}