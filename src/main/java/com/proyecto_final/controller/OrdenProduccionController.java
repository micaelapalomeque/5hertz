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

<<<<<<< HEAD
	OrdenProduccionService ordenProduccionService;
	
	public OrdenProduccionController(OrdenProduccionService ordenProduccionService) {
		this.ordenProduccionService = ordenProduccionService;
	}
	
	@PostMapping("/crear")
	public void crearOp(@RequestBody OrdenProduccion op) {
		ordenProduccionService.crearOp(op.getIdAlmacen(), op.getSku(), op.getCantidad(), op.getResponsable());
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
	public void consumirOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.consumirOp(request.getIdOp(), request.getResponsable());
	}
	
	@PutMapping("/cancelar")
	public void cancelarOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.cancelarOp(request.getIdOp(), request.getResponsable());
	}
	
	@PutMapping("/pausar")
	public void inactivarOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.pausarOp(request.getIdOp(), request.getResponsable());
	}
	
	@PutMapping("/reanudar")
	public void reanudarOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.reanudarOp(request.getIdOp(), request.getResponsable());
	}
	
	@GetMapping("consultar-recursos")
	public HashMap<String, Integer> consultarRecursosParaProducir(@RequestBody ConsultarRecursosRequest request) {
		return ordenProduccionService.calcularRecursosParaFabricar(request.getSku(), request.getCantidad());
	}
	
=======
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
>>>>>>> 0556cc9d964ed704f9de0d6cb6eb8e80acfa2551
}

