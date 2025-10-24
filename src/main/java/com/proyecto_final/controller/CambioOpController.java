package com.proyecto_final.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_final.service.CambioOpService;
import com.proyecto_final.model.CambioOp;
import java.util.List;

@RestController
@RequestMapping("/cambio-op")
public class CambioOpController {
	
	CambioOpService cambioOpService;
	
	public CambioOpController(CambioOpService cambioOpService) {
		this.cambioOpService = cambioOpService;
	}
	
	@GetMapping
	public List<CambioOp> consultarTodos() {
		return cambioOpService.consultarTodos();
	}
	
	@GetMapping("/{idOp}")
	public List <CambioOp> consultarOp(@PathVariable int idOp) {
		return cambioOpService.consultarCambios(idOp);
	}

}
