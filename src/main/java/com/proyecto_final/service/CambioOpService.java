package com.proyecto_final.service;

import org.springframework.stereotype.Service;
import com.proyecto_final.repository.CambioOpRepository;
import java.util.List;
import com.proyecto_final.model.CambioOp;

@Service
public class CambioOpService {

	private CambioOpRepository cambioOpRepository;
	
	public CambioOpService(CambioOpRepository cambioOpRepository) {
		this.cambioOpRepository = cambioOpRepository;
	}
	
	public void registrarCambio(int idOp, String estado, String responsable) {
		CambioOp cambio = new CambioOp(idOp, estado, responsable);
		cambioOpRepository.save(cambio);
	}
	
	public List<CambioOp> consultarCambios(int idOp) {
		return cambioOpRepository.findByIdOpOrderByFechaCambioDesc(idOp);
	}
	
	public List<CambioOp> consultarTodos() {
		return cambioOpRepository.findAll();
	}
}
