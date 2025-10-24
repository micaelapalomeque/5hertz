package com.proyecto_final.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.proyecto_final.repository.OrdenProduccionRepository;
import com.proyecto_final.model.OrdenProduccion;

@Service
public class OrdenProduccionService {
	
	private CambioOpService cambioOpService;
	private OrdenProduccionRepository ordenProduccionRepository;
	
	public OrdenProduccionService(OrdenProduccionRepository ordenProduccionRepository, CambioOpService cambioOpService) {
		this.ordenProduccionRepository = ordenProduccionRepository;
		this.cambioOpService = cambioOpService;
	}
	
	private boolean existeOp(int idOp) {
		return ordenProduccionRepository.existsById(idOp);
	}
	
	//Solo la crea. La registra en estado "planificada"
	public void crearOp(int idAlmacen, String sku, int cantidad, String responsable) {
		OrdenProduccion op = new OrdenProduccion(idAlmacen, sku, cantidad, "planificada", responsable);
		ordenProduccionRepository.save(op);
		cambioOpService.registrarCambio(op.getIdOp(), "planificada", responsable);
	}
	
	//Posibles estados: planificada - en produccion - consumida - cancelada
	public void cambiarEstado(int idOp, String estado, String responsable) {
		if(existeOp(idOp)) {
			//Delegar accion al metodo privado que corresponda
		}
	}
	
	private void setEstadoActiva(int idOp, String responsable) {
		//Consultar si el estado anterior es el correcto
		//Consultar si hay stock disponible de todos los materiales
		//ReservarStock
		//Cambiar el estado y almacenarlo
	}
	
	private void setEstadoConsumida(int idOp, String responsable) {
		//Consultar si el estado anterior es el correcto
		//Consumir stock
		//Cambiar el estado y almacenarlo
	}
	
	private void setEstadoCancelada(int idOp, String responsable) {
		//Consultar si el estado anterior es el correcto
		//Cambiar el estado y almacenarlo
	}

}
