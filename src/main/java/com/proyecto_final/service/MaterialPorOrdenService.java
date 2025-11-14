package com.proyecto_final.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.MaterialPorOrden;
import com.proyecto_final.model.RegistroDesperdicio;
import com.proyecto_final.model.ResumenDesperdicio;
import com.proyecto_final.repository.MaterialPorOrdenRepository;
import com.proyecto_final.repository.RegistroDesperdicioRepository;
import com.proyecto_final.repository.ResumenDesperdicioRepository;

@Service
public class MaterialPorOrdenService {

    private final MaterialPorOrdenRepository materialPorOrdenRepository;
    private final RegistroDesperdicioRepository registroDesperdicioRepository;
    private final ResumenDesperdicioRepository resumenDesperdicioRepository;

    public MaterialPorOrdenService(MaterialPorOrdenRepository materialPorOrdenRepository, RegistroDesperdicioRepository registroDesperdicioRepository, ResumenDesperdicioRepository resumenDesperdicioRepository) {
        this.materialPorOrdenRepository = materialPorOrdenRepository;
        this.registroDesperdicioRepository = registroDesperdicioRepository;
        this.resumenDesperdicioRepository = resumenDesperdicioRepository;
    }

    public void crearRegistroInicial(int idOp, String sku, int cantidadReservada) {
        MaterialPorOrden material = new MaterialPorOrden(idOp, sku, cantidadReservada);
        materialPorOrdenRepository.save(material);
    }

    public boolean registrarDesperdicio(int idOp, String sku, int cantidadDesperdiciada, String motivo, String estacion, String operario) {
        System.out.println("Buscando material: idOp=" + idOp + ", sku=" + sku);
        Optional<MaterialPorOrden> opt = materialPorOrdenRepository.findByIdOpAndSku(idOp, sku);
        if (opt.isEmpty()) {
            System.out.println("No se encontró el material en material_por_op para idOp=" + idOp + ", sku=" + sku);
            return false;
        }
        System.out.println("Material encontrado: " + opt.get().getCantidadReservada() + " reservada, " + opt.get().getCantidadDesperdiciada() + " desperdiciada");
        
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
        
        // Guardar detalles del desperdicio
        RegistroDesperdicio registro = new RegistroDesperdicio(idOp, sku, cantidadDesperdiciada, motivo, "", estacion, operario);
        registroDesperdicioRepository.save(registro);
        
        // Insertar en resumen de desperdicio
        actualizarResumenDesperdicio(idOp, sku, cantidadDesperdiciada, motivo);
        
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
    
    public List<Map<String, Object>> obtenerEstadisticasDesperdicio() {
        List<Object[]> resultados = registroDesperdicioRepository.findMotivosMasFrecuentes();
        List<Map<String, Object>> estadisticas = new java.util.ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("motivo", resultado[0]);
            stat.put("cantidad", resultado[1]);
            estadisticas.add(stat);
        }
        
        return estadisticas;
    }
    
    public List<Map<String, Object>> obtenerReporteOrdenes() {
        // Obtener todas las órdenes activas/terminadas con sus desperdicios
        List<Object[]> resultados = materialPorOrdenRepository.findOrdenesConDesperdicio();
        List<Map<String, Object>> reporte = new java.util.ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Map<String, Object> orden = new HashMap<>();
            orden.put("idOp", resultado[0]);
            orden.put("sku", resultado[1]);
            orden.put("estado", resultado[2]);
            orden.put("totalDesperdiciado", resultado[3] != null ? resultado[3] : 0);
            reporte.add(orden);
        }
        
        return reporte;
    }
    
    public Map<String, Object> obtenerEstadisticasGlobales() {
        Map<String, Object> stats = new HashMap<>();
        
        // SKU más desperdiciado (desde resumen_desperdicio)
        List<Object[]> skuStats = resumenDesperdicioRepository.findTop3ByOrderByGramosDesc();
        if (!skuStats.isEmpty()) {
            stats.put("skuMasDesperdiciado", skuStats.get(0)[0]);
            stats.put("gramosMasDesperdiciado", skuStats.get(0)[1]);
        }
        
        // Motivo más frecuente (desde resumen_desperdicio)
        List<Object[]> motivoStats = resumenDesperdicioRepository.findMotivosMasFrecuentesResumen();
        if (!motivoStats.isEmpty()) {
            stats.put("motivoMasFrecuente", motivoStats.get(0)[0]);
            stats.put("vecesMotivo", motivoStats.get(0)[1]);
        }
        
        return stats;
    }
    
    private void actualizarResumenDesperdicio(int idOp, String sku, int cantidadDesperdiciada, String motivo) {
        // Insertar nuevo registro por cada desperdicio
        ResumenDesperdicio nuevoResumen = new ResumenDesperdicio(idOp, motivo, sku, cantidadDesperdiciada);
        resumenDesperdicioRepository.save(nuevoResumen);
    }
    
    public List<Map<String, Object>> obtenerTop3Desperdicios() {
        List<Object[]> resultados = resumenDesperdicioRepository.findTop3ByOrderByGramosDesc();
        List<Map<String, Object>> top3 = new java.util.ArrayList<>();
        
        int limit = Math.min(3, resultados.size());
        for (int i = 0; i < limit; i++) {
            Object[] resultado = resultados.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("sku", resultado[0]);
            item.put("totalGramos", resultado[1]);
            top3.add(item);
        }
        
        return top3;
    }
    
    public Optional<MaterialPorOrden> buscarPorOpYSku(int idOp, String sku) {
        return materialPorOrdenRepository.findByIdOpAndSku(idOp, sku);
    }
    
    public boolean registrarDesperdicioCompleto(int idOp, String sku, int cantidadDesperdiciada, String motivo, String estacion, String operario) {
        try {
            // 1. Buscar o crear registro en material_por_op
            Optional<MaterialPorOrden> opt = materialPorOrdenRepository.findByIdOpAndSku(idOp, sku);
            MaterialPorOrden material;
            
            if (opt.isEmpty()) {
                // Crear nuevo registro con cantidad reservada = 0
                material = new MaterialPorOrden(idOp, sku, 0);
                materialPorOrdenRepository.save(material);
            } else {
                material = opt.get();
            }
            
            // 2. Actualizar desperdicio en material_por_op
            int nuevoDesperdicio = material.getCantidadDesperdiciada() + cantidadDesperdiciada;
            material.setCantidadDesperdiciada(nuevoDesperdicio);
            
            // Recalcular pendiente
            int nuevaPendiente = material.getCantidadReservada() - material.getCantidadConsumida() - nuevoDesperdicio;
            material.setCantidadPendiente(Math.max(nuevaPendiente, 0));
            
            materialPorOrdenRepository.save(material);
            
            // 3. Guardar detalle en registro_desperdicio
            RegistroDesperdicio registro = new RegistroDesperdicio(idOp, sku, cantidadDesperdiciada, motivo, "", estacion, operario);
            registroDesperdicioRepository.save(registro);
            
            // 4. Insertar en resumen
            actualizarResumenDesperdicio(idOp, sku, cantidadDesperdiciada, motivo);
            
            return true;
        } catch (Exception e) {
            System.out.println("Error registrando desperdicio completo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}