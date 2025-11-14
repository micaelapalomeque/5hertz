package com.proyecto_final.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.MaterialPorOp;
import com.proyecto_final.model.ResumenDesperdicio;
import java.util.Optional;
import com.proyecto_final.service.MaterialPorOpService;
import com.proyecto_final.service.MaterialPorOrdenService;

@RestController
@RequestMapping("/material-op")
public class MaterialPorOpController {

    private final MaterialPorOpService materialPorOpService;
    private final MaterialPorOrdenService materialPorOrdenService;

    public MaterialPorOpController(MaterialPorOpService materialPorOpService, MaterialPorOrdenService materialPorOrdenService) {
        this.materialPorOpService = materialPorOpService;
        this.materialPorOrdenService = materialPorOrdenService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialPorOp>> consultarReservas() {
        return ResponseEntity.ok(materialPorOpService.consultarReservas());
    }

    @GetMapping("/op/{idOp}")
    public ResponseEntity<?> consultarReservasPorOp(@PathVariable int idOp) {
        if (idOp <= 0) {
            return ResponseEntity.badRequest().body("El id de la orden debe ser mayor a cero.");
        }
        List<MaterialPorOp> lista = materialPorOpService.consultarReservasPorOp(idOp);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reservado")
    public ResponseEntity<?> consultarMaterialReservado(
            @RequestParam int idOp,
            @RequestParam String sku) {

        if (idOp <= 0 || sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Datos invalidos: idOp > 0 y sku no vacio.");
        }

        int cantidad = materialPorOpService.consultarMaterialReservado(idOp, sku);
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/consumido")
    public ResponseEntity<?> consultarMaterialConsumido(
            @RequestParam int idOp,
            @RequestParam String sku) {

        if (idOp <= 0 || sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Datos invalidos: idOp > 0 y sku no vacio.");
        }

        int cantidad = materialPorOpService.consultarMaterialConsumido(idOp, sku);
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/pendiente")
    public ResponseEntity<?> consultarCantidadPendiente(
            @RequestParam int idOp,
            @RequestParam String sku) {

        if (idOp <= 0 || sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Datos invalidos: idOp > 0 y sku no vacio.");
        }

        int cantidad = materialPorOpService.consultarCantidadPendiente(idOp, sku);
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/diferencia")
    public ResponseEntity<?> consultarDiferencia(
            @RequestParam int idOp,
            @RequestParam String sku) {

        if (idOp <= 0 || sku == null || sku.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Datos invalidos: idOp > 0 y sku no vacio.");
        }

        int diferencia = materialPorOpService.consultarDiferencia(idOp, sku);
        return ResponseEntity.ok(diferencia);
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> registrarReserva(
            @RequestParam int idOp,
            @RequestParam String sku,
            @RequestParam int cantidad) {

        boolean ok = materialPorOpService.registrarReserva(idOp, sku, cantidad);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo registrar la reserva. Verifica idOp, sku y cantidad.");
        }

        return ResponseEntity.ok("Reserva registrada correctamente.");
    }

    @PutMapping("/reservado")
    public ResponseEntity<?> modificarCantidadReservada(
            @RequestParam int idOp,
            @RequestParam String sku,
            @RequestParam int cantidad) {

        boolean ok = materialPorOpService.modificarCantidadReservada(idOp, sku, cantidad);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo modificar la cantidad reservada. Verifica idOp, sku y cantidad.");
        }

        return ResponseEntity.ok("Cantidad reservada modificada correctamente.");
    }

    @PutMapping("/consumido")
    public ResponseEntity<?> modificarCantidadConsumida(
            @RequestParam int idOp,
            @RequestParam String sku,
            @RequestParam int cantidad) {

        boolean ok = materialPorOpService.modificarCantidadConsumida(idOp, sku, cantidad);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo modificar la cantidad consumida. Verifica idOp, sku, cantidad y que no supere la reservada.");
        }

        return ResponseEntity.ok("Cantidad consumida modificada correctamente.");
    }

    @PutMapping("/pendiente")
    public ResponseEntity<?> modificarCantidadPendiente(
            @RequestParam int idOp,
            @RequestParam String sku,
            @RequestParam int cantidad) {

        boolean ok = materialPorOpService.modificarCantidadPendiente(idOp, sku, cantidad);

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo modificar la cantidad pendiente. Verifica idOp, sku, cantidad y que no supere la reservada.");
        }

        return ResponseEntity.ok("Cantidad pendiente modificada correctamente.");
    }

    public static class RegistrarDesperdicioRequest {
        private int idOp;
        private String sku;
        private int cantidadDesperdiciada;
        private String motivo;

        private String estacion;
        private String operario;
        private java.util.List<MaterialDesperdicio> materiales;
        
        public static class MaterialDesperdicio {
            private String sku;
            private int desperdicio_gramos;
            
            public String getSku() { return sku; }
            public void setSku(String sku) { this.sku = sku; }
            public int getDesperdicio_gramos() { return desperdicio_gramos; }
            public void setDesperdicio_gramos(int desperdicio_gramos) { this.desperdicio_gramos = desperdicio_gramos; }
        }

        // Getters y setters
        public int getIdOp() { return idOp; }
        public void setIdOp(int idOp) { this.idOp = idOp; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getCantidadDesperdiciada() { return cantidadDesperdiciada; }
        public void setCantidadDesperdiciada(int cantidadDesperdiciada) { this.cantidadDesperdiciada = cantidadDesperdiciada; }
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public String getEstacion() { return estacion; }
        public void setEstacion(String estacion) { this.estacion = estacion; }
        public String getOperario() { return operario; }
        public void setOperario(String operario) { this.operario = operario; }
        public java.util.List<MaterialDesperdicio> getMateriales() { return materiales; }
        public void setMateriales(java.util.List<MaterialDesperdicio> materiales) { this.materiales = materiales; }
    }

    @PutMapping("/registrar-desperdicio")
    public ResponseEntity<?> registrarDesperdicio(@RequestBody RegistrarDesperdicioRequest request) {
        System.out.println("=== REGISTRO DESPERDICIO ===");
        System.out.println("idOp: " + request.getIdOp());
        System.out.println("sku: " + request.getSku());
        System.out.println("cantidad: " + request.getCantidadDesperdiciada());
        System.out.println("motivo: " + request.getMotivo());
        System.out.println("estacion: " + request.getEstacion());
        System.out.println("operario: " + request.getOperario());
        
        if (request.getIdOp() <= 0 || request.getSku() == null || request.getSku().isBlank() || request.getCantidadDesperdiciada() <= 0) {
            String error = "Datos inválidos: idOp=" + request.getIdOp() + ", sku=" + request.getSku() + ", cantidad=" + request.getCantidadDesperdiciada();
            System.out.println("ERROR VALIDACION: " + error);
            return ResponseEntity.badRequest().body(error);
        }

        try {
            // Registrar en todas las tablas necesarias
            boolean ok = materialPorOrdenService.registrarDesperdicioCompleto(
                request.getIdOp(), 
                request.getSku(), 
                request.getCantidadDesperdiciada(),
                request.getMotivo(),
                request.getEstacion(),
                request.getOperario()
            );
            
            if (!ok) {
                System.out.println("ERROR: registrarDesperdicioCompleto retornó false");
                return ResponseEntity.badRequest().body("Error al registrar desperdicio - operación falló");
            }
            
            System.out.println("SUCCESS: Desperdicio registrado correctamente");
            return ResponseEntity.ok("Desperdicio registrado correctamente.");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al registrar desperdicio: " + e.getMessage());
        }
    }
    
    @GetMapping("/estadisticas-desperdicio")
    public ResponseEntity<List<Map<String, Object>>> obtenerEstadisticasDesperdicio() {
        try {
            List<Map<String, Object>> estadisticas = materialPorOrdenService.obtenerEstadisticasDesperdicio();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/top-desperdicios")
    public ResponseEntity<List<Map<String, Object>>> obtenerTopDesperdicios() {
        try {
            List<Map<String, Object>> top3 = materialPorOrdenService.obtenerTop3Desperdicios();
            return ResponseEntity.ok(top3);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/reporte-ordenes")
    public ResponseEntity<List<Map<String, Object>>> obtenerReporteOrdenes() {
        try {
            List<Map<String, Object>> reporte = materialPorOrdenService.obtenerReporteOrdenes();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/estadisticas-globales")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGlobales() {
        try {
            Map<String, Object> stats = materialPorOrdenService.obtenerEstadisticasGlobales();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PutMapping("/registrar-desperdicios-lote")
    public ResponseEntity<?> registrarDesperdiciosLote(@RequestBody RegistrarDesperdicioRequest request) {
        System.out.println("=== REGISTRO DESPERDICIOS LOTE ===");
        System.out.println("idOp: " + request.getIdOp());
        System.out.println("motivo: " + request.getMotivo());
        System.out.println("materiales: " + (request.getMateriales() != null ? request.getMateriales().size() : 0));
        
        if (request.getIdOp() <= 0 || request.getMateriales() == null || request.getMateriales().isEmpty()) {
            return ResponseEntity.badRequest().body("Datos inválidos: idOp > 0 y materiales no vacío");
        }

        try {
            int registrosExitosos = 0;
            for (RegistrarDesperdicioRequest.MaterialDesperdicio material : request.getMateriales()) {
                if (material.getDesperdicio_gramos() > 0) {
                    boolean ok = materialPorOrdenService.registrarDesperdicioCompleto(
                        request.getIdOp(), 
                        material.getSku(), 
                        material.getDesperdicio_gramos(),
                        request.getMotivo(),
                        request.getEstacion(),
                        request.getOperario()
                    );
                    
                    if (ok) {
                        registrosExitosos++;
                        System.out.println("SUCCESS: " + material.getSku() + " - " + material.getDesperdicio_gramos() + "g");
                    } else {
                        System.out.println("ERROR: " + material.getSku() + " - " + material.getDesperdicio_gramos() + "g");
                    }
                }
            }
            
            if (registrosExitosos == 0) {
                return ResponseEntity.badRequest().body("No se pudo registrar ningún desperdicio");
            }
            
            return ResponseEntity.ok("Desperdicios registrados: " + registrosExitosos + " de " + request.getMateriales().size());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PutMapping("/consumir-material")
    public ResponseEntity<?> consumirMaterial(@RequestBody RegistrarDesperdicioRequest request) {
        if (request.getIdOp() <= 0 || request.getSku() == null || request.getSku().isBlank() || request.getCantidadDesperdiciada() <= 0) {
            return ResponseEntity.badRequest().body("Datos inválidos: idOp > 0, sku no vacío y cantidad > 0");
        }

        try {
            boolean ok = materialPorOrdenService.consumirMaterial(request.getIdOp(), request.getSku(), request.getCantidadDesperdiciada());
            
            if (!ok) {
                return ResponseEntity.badRequest().body("No se pudo consumir el material. Verifica la cantidad disponible.");
            }
            
            return ResponseEntity.ok("Material consumido correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al consumir material: " + e.getMessage());
        }
    }
}
