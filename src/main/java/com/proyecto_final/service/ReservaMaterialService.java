package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.repository.ReservaMaterialRepository;
import com.proyecto_final.model.ReservaMaterial;

@Service
public class ReservaMaterialService {
	
	private ReservaMaterialRepository reservaMaterialRepository;
	
	public ReservaMaterialService(ReservaMaterialRepository reservaMaterialRepository) {
		this.reservaMaterialRepository = reservaMaterialRepository;
	}
	
	public List<ReservaMaterial> consultarReservas() {
		return reservaMaterialRepository.findAll();
	}
	
	public List<ReservaMaterial> consultarReservasPorOp(int idOp) {
		return reservaMaterialRepository.findByIdOp(idOp);
	}
	
	public Integer consultarMaterialReservado(int idOp, String sku) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			ReservaMaterial reserva = opt.get();
		return reserva.getCantidadReservada();
		}
		return 0;
	}
	
	public Integer consultarMaterialConsumido(int idOp, String sku) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			ReservaMaterial reserva = opt.get();
		return reserva.getCantidadConsumida();
		}
		return 0;
	}
	
	public int consultarDiferencia(int idOp, String sku) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			return opt.get().getCantidadReservada() - opt.get().getCantidadConsumida();
		}
		return 0;
	}
	
	public int consultarCantidadPendiente(int idOp, String sku) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			return opt.get().getCantidadPendiente();		
		}
		return 0;
	}
	
	public void registrarReserva(int idOp, String sku, int cantidadReservada) {
		ReservaMaterial reserva = new ReservaMaterial(idOp, sku, cantidadReservada);
		reservaMaterialRepository.save(reserva);
	}
	
	public void modificarCantidadReservada(int idOp, String sku, int cantidadReservada) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			ReservaMaterial reserva = opt.get();
			reserva.setCantidadReservada(cantidadReservada);
			reservaMaterialRepository.save(reserva);		
		}
	}
	
	public void modificarCantidadConsumida(int idOp, String sku, int cantidadConsumida) {
		Optional<ReservaMaterial> opt = reservaMaterialRepository.findByIdOpSku(idOp, sku);
		if(opt.isPresent()) {
			ReservaMaterial reserva = opt.get();
			reserva.setCantidadConsumida(cantidadConsumida);
			reservaMaterialRepository.save(reserva);
		}
	}


}
