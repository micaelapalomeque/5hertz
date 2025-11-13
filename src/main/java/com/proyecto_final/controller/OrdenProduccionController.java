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
    public ResponseEntity<?> crearOp(@RequestBody OrdenProduccion op) {

        boolean ok = ordenProduccionService.crearOp(
                op.getIdAlmacen(),
                op.getSku(),
                op.getCantidad(),
                op.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo crear la orden. Verifica idAlmacen, sku, cantidad y responsable.");
        }

        return ResponseEntity.ok("Orden creada correctamente.");
    }

    @GetMapping("/consultar/{idOp}")
    public ResponseEntity<?> consultarOp(@PathVariable int idOp) {
        Optional<OrdenProduccion> opt = ordenProduccionService.consultarOp(idOp);

        return opt.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/consultar/todas")
    public ResponseEntity<List<OrdenProduccion>> consultarTodas() {
        return ResponseEntity.ok(ordenProduccionService.consultarTodas());
    }

    @PutMapping("/activar")
    public ResponseEntity<?> activarOp(@RequestBody CambiarEstadoOpRequest request) {

        boolean ok = ordenProduccionService.activarOp(
                request.getIdOp(),
                request.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo activar la orden. Verifica estado actual, stock disponible o datos enviados.");
        }

        return ResponseEntity.ok("Orden activada correctamente.");
    }

    @PutMapping("/consumir")
    public ResponseEntity<?> consumirOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean ok = ordenProduccionService.consumirOp(
                request.getIdOp(),
                request.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo consumir la orden. Verifica estado actual y datos enviados.");
        }

        return ResponseEntity.ok("Orden consumida correctamente.");
    }

    @PutMapping("/cancelar")
    public ResponseEntity<?> cancelarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean ok = ordenProduccionService.cancelarOp(
                request.getIdOp(),
                request.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo cancelar la orden. Verifica estado actual.");
        }

        return ResponseEntity.ok("Orden cancelada correctamente.");
    }

    @PutMapping("/pausar")
    public ResponseEntity<?> pausarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean ok = ordenProduccionService.pausarOp(
                request.getIdOp(),
                request.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo pausar la orden. Verifica estado actual.");
        }

        return ResponseEntity.ok("Orden pausada correctamente.");
    }

    @PutMapping("/reanudar")
    public ResponseEntity<?> reanudarOp(@RequestBody CambiarEstadoOpRequest request) {
        boolean ok = ordenProduccionService.reanudarOp(
                request.getIdOp(),
                request.getResponsable()
        );

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body("No se pudo reanudar la orden. Verifica stock disponible y estado actual.");
        }

        return ResponseEntity.ok("Orden reanudada correctamente.");
    }

    @PostMapping("/consultar-recursos")
    public ResponseEntity<?> consultarRecursosParaProducir(@RequestBody ConsultarRecursosRequest request) {

        if (request.getSku() == null || request.getSku().isBlank() || request.getCantidad() <= 0) {
            return ResponseEntity.badRequest()
                    .body("Datos invalidos: sku no vacio, cantidad > 0.");
        }

        Map<String, Integer> recursos =
                ordenProduccionService.calcularRecursosParaFabricar(request.getSku(), request.getCantidad());

        if (recursos.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El producto no posee BOM o los datos enviados no son validos.");
        }

        return ResponseEntity.ok(recursos);
    }
}

