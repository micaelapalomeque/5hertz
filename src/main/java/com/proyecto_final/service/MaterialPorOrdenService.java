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
        
        // Actualizar resumen de desperdicio
        actualizarResumenDesperdicio(idOp);
        
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
    
    private void actualizarResumenDesperdicio(int idOp) {
        // Obtener todos los desperdicios de esta orden
        List<RegistroDesperdicio> desperdicios = registroDesperdicioRepository.findByIdOp(idOp);
        
        if (desperdicios.isEmpty()) return;
        
        // Encontrar motivo más frecuente
        Map<String, Integer> motivoCount = new HashMap<>();
        for (RegistroDesperdicio d : desperdicios) {
            motivoCount.put(d.getMotivo(), motivoCount.getOrDefault(d.getMotivo(), 0) + 1);
        }
        String motivoPrincipal = motivoCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
        
        // Encontrar SKU con mayor desperdicio
        Map<String, Integer> skuDesperdicio = new HashMap<>();
        for (RegistroDesperdicio d : desperdicios) {
            skuDesperdicio.put(d.getSku(), skuDesperdicio.getOrDefault(d.getSku(), 0) + d.getCantidadDesperdiciada());
        }
        String skuMayor = skuDesperdicio.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
        int gramosMayor = skuDesperdicio.getOrDefault(skuMayor, 0);
        
        // Actualizar o crear resumen
        Optional<ResumenDesperdicio> existente = resumenDesperdicioRepository.findByIdOp(idOp);
        if (existente.isPresent()) {
            ResumenDesperdicio resumen = existente.get();
            resumen.setMotivoPrincipal(motivoPrincipal);
            resumen.setSkuMayorDesperdicio(skuMayor);
            resumen.setGramosDesperdiciados(gramosMayor);
            resumenDesperdicioRepository.save(resumen);
        } else {
            ResumenDesperdicio nuevo = new ResumenDesperdicio(idOp, motivoPrincipal, skuMayor, gramosMayor);
            resumenDesperdicioRepository.save(nuevo);
        }
    }
    
    public List<ResumenDesperdicio> obtenerTop3Desperdicios() {
        List<ResumenDesperdicio> todos = resumenDesperdicioRepository.findTop3ByOrderByGramosDesc();
        return todos.size() > 3 ? todos.subList(0, 3) : todos;
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
            
            // 4. Actualizar resumen
            actualizarResumenDesperdicio(idOp);
            
            return true;
        } catch (Exception e) {
            System.out.println("Error registrando desperdicio completo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}