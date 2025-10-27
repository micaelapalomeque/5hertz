package com.proyecto_final.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.service.OrdenProduccionService;
import request.CambiarEstadoOpRequest;

@Controller
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
	
	@GetMapping("/consultar")
	public Optional<OrdenProduccion> consultarOp(@PathVariable int idOp) {
		return ordenProduccionService.consultarOp(idOp);
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
	
	@PutMapping("/inactiva")
	public void inactivarOp(@RequestBody CambiarEstadoOpRequest request) {
		ordenProduccionService.inactivarOp(request.getIdOp(), request.getResponsable());
	}
	
}
