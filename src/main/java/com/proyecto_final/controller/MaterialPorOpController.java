package com.proyecto_final.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto_final.model.MaterialPorOp;
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
        private String observaciones;
        private String estacion;
        private String operario;

        // Getters y setters
        public int getIdOp() { return idOp; }
        public void setIdOp(int idOp) { this.idOp = idOp; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getCantidadDesperdiciada() { return cantidadDesperdiciada; }
        public void setCantidadDesperdiciada(int cantidadDesperdiciada) { this.cantidadDesperdiciada = cantidadDesperdiciada; }
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        public String getEstacion() { return estacion; }
        public void setEstacion(String estacion) { this.estacion = estacion; }
        public String getOperario() { return operario; }
        public void setOperario(String operario) { this.operario = operario; }
    }

    @PutMapping("/registrar-desperdicio")
    public ResponseEntity<?> registrarDesperdicio(@RequestBody RegistrarDesperdicioRequest request) {
        if (request.getIdOp() <= 0 || request.getSku() == null || request.getSku().isBlank() || request.getCantidadDesperdiciada() <= 0) {
            return ResponseEntity.badRequest().body("Datos inválidos: idOp > 0, sku no vacío y cantidadDesperdiciada > 0");
        }

        try {
            boolean ok = materialPorOrdenService.registrarDesperdicio(request.getIdOp(), request.getSku(), request.getCantidadDesperdiciada());
            
            if (!ok) {
                return ResponseEntity.badRequest().body("No se pudo registrar el desperdicio. Verifica que no supere la cantidad reservada disponible.");
            }
            
            return ResponseEntity.ok("Desperdicio registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar desperdicio: " + e.getMessage());
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
