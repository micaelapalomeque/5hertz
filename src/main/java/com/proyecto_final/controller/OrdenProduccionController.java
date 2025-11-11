package com.proyecto_final.controller;

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
	public Optional<OrdenProduccion> consultarOp(@PathVariable int idOp) {
		return ordenProduccionService.consultarOp(idOp);
	}
	
	@GetMapping("/consultar/todas")
	public List<OrdenProduccion> consultarTodas() {
		return ordenProduccionService.consultarTodas();
	}
	
	@PutMapping("/activar")
	public void activarOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.activarOp(request.getIdOp(), request.getResponsable());
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
	
}
