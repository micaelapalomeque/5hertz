package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.MaterialPorOp;
import com.proyecto_final.repository.MaterialPorOpRepository;

@Service
public class MaterialPorOpService {

<<<<<<< HEAD
	public void modificarCantidadPendiente(int idOp, String sku, int cantidadPendiente) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
			reserva.setCantidadPendiente(cantidadPendiente);
			materialPorOpRepository.save(reserva);
		}
	}
	
	public void registrarDesperdicio(int idOp, String sku, int cantidadDesperdiciada) {
		Optional<MaterialPorOp> opt = materialPorOpRepository.findByIdOpAndSku(idOp, sku);
		if(opt.isPresent()) {
			MaterialPorOp reserva = opt.get();
			reserva.setCantidadDesperdiciada(reserva.getCantidadDesperdiciada() + cantidadDesperdiciada);
			materialPorOpRepository.save(reserva);
		}
	}
=======
    private final MaterialPorOpRepository materialPorOpRepository;
>>>>>>> 0556cc9d964ed704f9de0d6cb6eb8e80acfa2551

    public MaterialPorOpService(MaterialPorOpRepository materialPorOpRepository) {
        this.materialPorOpRepository = materialPorOpRepository;
    }

    private boolean datosInvalidos(int idOp, String sku, int cantidad) {
        if (idOp <= 0) return true;
        if (sku == null || sku.isBlank()) return true;
        if (cantidad < 0) return true;
        return false;
    }

    private Optional<MaterialPorOp> obtenerRegistro(int idOp, String sku) {
        return materialPorOpRepository.findByIdOpAndSku(idOp, sku);
    }

    public List<MaterialPorOp> consultarReservas() {
        return materialPorOpRepository.findAll();
    }

    public List<MaterialPorOp> consultarReservasPorOp(int idOp) {
        return idOp > 0 ? materialPorOpRepository.findByIdOp(idOp) : List.of();
    }

    public Integer consultarMaterialReservado(int idOp, String sku) {
        return obtenerRegistro(idOp, sku)
                .map(MaterialPorOp::getCantidadReservada)
                .orElse(0);
    }

    public Integer consultarMaterialConsumido(int idOp, String sku) {
        return obtenerRegistro(idOp, sku)
                .map(MaterialPorOp::getCantidadConsumida)
                .orElse(0);
    }

    public int consultarDiferencia(int idOp, String sku) {
        return obtenerRegistro(idOp, sku)
                .map(r -> r.getCantidadReservada() - r.getCantidadConsumida())
                .orElse(0);
    }

    public int consultarCantidadPendiente(int idOp, String sku) {
        return obtenerRegistro(idOp, sku)
                .map(MaterialPorOp::getCantidadPendiente)
                .orElse(0);
    }

    public boolean registrarReserva(int idOp, String sku, int cantidadReservada) {
        if (datosInvalidos(idOp, sku, cantidadReservada)) return false;
        if (cantidadReservada == 0) return false;

        MaterialPorOp reserva = new MaterialPorOp(idOp, sku, cantidadReservada);
        materialPorOpRepository.save(reserva);

        return true;
    }

    public boolean modificarCantidadReservada(int idOp, String sku, int cantidadReservada) {
        if (datosInvalidos(idOp, sku, cantidadReservada)) return false;

        Optional<MaterialPorOp> opt = obtenerRegistro(idOp, sku);
        if (opt.isEmpty()) return false;

        MaterialPorOp reserva = opt.get();
        reserva.setCantidadReservada(cantidadReservada);

        int diferencia = cantidadReservada - reserva.getCantidadConsumida();
        reserva.setCantidadPendiente(Math.max(diferencia, 0));

        materialPorOpRepository.save(reserva);
        return true;
    }

    public boolean modificarCantidadConsumida(int idOp, String sku, int cantidadConsumida) {
        if (datosInvalidos(idOp, sku, cantidadConsumida)) return false;

        Optional<MaterialPorOp> opt = obtenerRegistro(idOp, sku);
        if (opt.isEmpty()) return false;

        MaterialPorOp reserva = opt.get();

        if (cantidadConsumida > reserva.getCantidadReservada()) return false;

        reserva.setCantidadConsumida(cantidadConsumida);

        int diferencia = reserva.getCantidadReservada() - cantidadConsumida;
        reserva.setCantidadPendiente(Math.max(diferencia, 0));

        materialPorOpRepository.save(reserva);
        return true;
    }

    public boolean modificarCantidadPendiente(int idOp, String sku, int cantidadPendiente) {
        if (datosInvalidos(idOp, sku, cantidadPendiente)) return false;

        Optional<MaterialPorOp> opt = obtenerRegistro(idOp, sku);
        if (opt.isEmpty()) return false;

        MaterialPorOp reserva = opt.get();

        if (cantidadPendiente > reserva.getCantidadReservada()) return false;

        reserva.setCantidadPendiente(cantidadPendiente);

        materialPorOpRepository.save(reserva);
        return true;
    }
}

