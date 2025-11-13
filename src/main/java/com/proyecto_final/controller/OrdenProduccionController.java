package com.proyecto_final.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.service.OrdenProduccionService;
import request.CambiarEstadoOpRequest;
import request.ConsultarRecursosRequest;

@RestController
@RequestMapping("/ordenes-produccion")
public class OrdenProduccionController {

    private final OrdenProduccionService ordenProduccionService;

    public OrdenProduccionController(OrdenProduccionService ordenProduccionService) {
        this.ordenProduccionService = ordenProduccionService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearOp(@RequestBody OrdenProduccion op) {
        boolean creada = ordenProduccionService.crearOp(op.getIdAlmacen(), op.getSku(), op.getCantidad(), op.getResponsable());
        if (creada) {
            return ResponseEntity.ok("Orden creada exitosamente");
        } else {
            return ResponseEntity.badRequest().body("Error al crear la orden");
        }
    }

    @GetMapping("/consultar/{idOp}")
    public OrdenProduccion consultarOp(@PathVariable int idOp) {
        return ordenProduccionService.consultarOp(idOp)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @GetMapping("/consultar/todas")
    public List<OrdenProduccion> consultarTodas() {
        return ordenProduccionService.consultarTodas();
    }

    @PutMapping("/activar")
    public String activarOp(@RequestBody CambiarEstadoOpRequest request) {
        try {
            System.out.println("=== CONTROLADOR: Iniciando activación orden " + request.getIdOp() + " ===");
            
            // Cambiar estado inmediatamente
            ordenProduccionService.cambiarEstadoRapido(request.getIdOp(), "activa", request.getResponsable());
            
            // Procesar reservas y lotes en segundo plano
            new Thread(() -> {
                try {
                    System.out.println("=== Procesando reservas y lotes en segundo plano ===");
                    ordenProduccionService.procesarReservasYLotes(request.getIdOp(), request.getResponsable());
                    System.out.println("=== Reservas y lotes completados ===");
                } catch (Exception e) {
                    System.err.println("Error en proceso asíncrono: " + e.getMessage());
                    e.printStackTrace();
                    // Si falla el proceso asíncrono, revertir estado
                    ordenProduccionService.cambiarEstadoRapido(request.getIdOp(), "planificada", request.getResponsable());
                }
            }).start();
            
            return "Orden activada. Procesando reservas...";
            
        } catch (Exception e) {
            System.err.println("=== ERROR EN CONTROLADOR ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/consumir")
    public ResponseEntity<String> consumirOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean resultado = ordenProduccionService.consumirOp(request.getIdOp(), request.getResponsable());
        return resultado ? ResponseEntity.ok("Orden consumida") : ResponseEntity.badRequest().body("Error al consumir orden");
    }

    @PutMapping("/cancelar")
    public ResponseEntity<String> cancelarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean resultado = ordenProduccionService.cancelarOp(request.getIdOp(), request.getResponsable());
        return resultado ? ResponseEntity.ok("Orden cancelada") : ResponseEntity.badRequest().body("Error al cancelar orden");
    }

    @PutMapping("/pausar")
    public ResponseEntity<String> pausarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean resultado = ordenProduccionService.pausarOp(request.getIdOp(), request.getResponsable());
        return resultado ? ResponseEntity.ok("Orden pausada") : ResponseEntity.badRequest().body("Error al pausar orden");
    }

    @PutMapping("/reanudar")
    public ResponseEntity<String> reanudarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean resultado = ordenProduccionService.reanudarOp(request.getIdOp(), request.getResponsable());
        return resultado ? ResponseEntity.ok("Orden reanudada") : ResponseEntity.badRequest().body("Error al reanudar orden");
    }

    @PostMapping("/consultar-recursos")
    public Map<String, Integer> consultarRecursosParaProducir(@RequestBody ConsultarRecursosRequest request) {
        return ordenProduccionService.calcularRecursosParaFabricar(request.getSku(), request.getCantidad());
    }
}

