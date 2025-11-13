package com.proyecto_final.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.service.OrdenProduccionService;
import request.CambiarEstadoOpRequest;
import request.ConsultarRecursosRequest;

@RestController
@RequestMapping("/ordenes-produccion")
public class OrdenProduccionController {

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
	
}
