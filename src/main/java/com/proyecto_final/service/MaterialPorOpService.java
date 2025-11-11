package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.repository.MaterialPorOpRepository;
import com.proyecto_final.model.MaterialPorOp;

@Service
public class MaterialPorOpService {
	
	private MaterialPorOpRepository materialPorOpRepository;
	
	public MaterialPorOpService(MaterialPorOpRepository reservaMaterialRepository) {
		this.materialPorOpRepository = reservaMaterialRepository;
	}
	
	public List<MaterialPorOp> consultarReservas() {
		return materialPorOpRepository.findAll();
	}
	
	public List<MaterialPorOp> consultarReservasPorOp(int idOp) {
		return materialPorOpRepository.findByIdOp(idOp);
	}
	
	public Integer consultarMaterialReservado(int idOp, String sku) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
		return reserva.getCantidadReservada();
		}
		return 0;
	}
	
	public Integer consultarMaterialConsumido(int idOp, String sku) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
		return reserva.getCantidadConsumida();
		}
		return 0;
	}
	
	public int consultarDiferencia(int idOp, String sku) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			return opt.get().getCantidadReservada() - opt.get().getCantidadConsumida();
		}
		return 0;
	}
	
	public int consultarCantidadPendiente(int idOp, String sku) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			return opt.get().getCantidadPendiente();		
		}
		return 0;
	}
	
	public void registrarReserva(int idOp, String sku, int cantidadReservada) {
		MaterialPorOp reserva = new MaterialPorOp(idOp, sku, cantidadReservada);
		materialPorOpRepository.save(reserva);
	}
	
	public void modificarCantidadReservada(int idOp, String sku, int cantidadReservada) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
			reserva.setCantidadReservada(cantidadReservada);
			materialPorOpRepository.save(reserva);		
		}
	}
	
	public void modificarCantidadConsumida(int idOp, String sku, int cantidadConsumida) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
			reserva.setCantidadConsumida(cantidadConsumida);
			materialPorOpRepository.save(reserva);
		}
	}

	public void modificarCantidadPendiente(int idOp, String sku, int cantidadPendiente) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
			reserva.setCantidadPendiente(cantidadPendiente);
			materialPorOpRepository.save(reserva);
		}
	}

}
