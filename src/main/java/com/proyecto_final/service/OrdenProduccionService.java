package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.repository.OrdenProduccionRepository;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.model.Bom;

@Service
public class OrdenProduccionService {
	
	private CambioOpService cambioOpService;
	private StockAlmacenService stockAlmacenService;
	private BomService bomService;
	private OrdenProduccionRepository ordenProduccionRepository;

	public OrdenProduccionService(OrdenProduccionRepository ordenProduccionRepository, 
			CambioOpService cambioOpService, StockAlmacenService stockAlmacenService, BomService bomService) {
		this.ordenProduccionRepository = ordenProduccionRepository;
		this.cambioOpService = cambioOpService;
		this.stockAlmacenService = stockAlmacenService;
		this.bomService = bomService;
	}

	public void crearOp(int idAlmacen, String sku, int cantidad, String responsable) {
		OrdenProduccion op = new OrdenProduccion(idAlmacen, sku, cantidad, "planificada", responsable);
		ordenProduccionRepository.save(op);
		cambioOpService.registrarCambio(op.getIdOp(), "planificada", responsable);
	}
	
	public Optional<OrdenProduccion> consultarOp(int idOp) {
		return ordenProduccionRepository.findById(idOp);
	}
	
	private boolean hayStockParaFabricar(int idAlmacen, String skuProductoFinal, int cantidad) {
		List<Bom> listaMateriales = bomService.obtenerListaMateriales(skuProductoFinal);
		for(Bom bom : listaMateriales) {
			if(!stockAlmacenService.hayStockDisponible(bom.getSkuMaterial(), idAlmacen, bom.getCanPorUnidad() * cantidad)) {
				return false;
			}
		}
		return true;
	}

	public void activarOp(int idOp, String responsable) {
	    Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if (!(op.getEstado().equals("planificada") || op.getEstado().equals("pausada")) || !hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad())) {
	        return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for (Bom bom : listaMateriales) {
	        stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), op.getCantidad() * bom.getCanPorUnidad());
	    }
	    
	    op.setEstado("activa");
	    ordenProduccionRepository.save(op);
	    cambioOpService.registrarCambio(idOp, "activa", responsable);
	}
	
	public void consumirOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(!op.getEstado().equals("activa")) {
	    	return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for(Bom bom : listaMateriales) {
	    	stockAlmacenService.consumirMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), bom.getCanPorUnidad() * op.getCantidad());
	    }
	    
	    op.setEstado("consumida");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "consumida", responsable);
	}
	
	//Falta implementar registro de material
	public void cancelarOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(op.getEstado().equals("consumida")) {
	    	return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for(Bom bom : listaMateriales) {
	    	stockAlmacenService.liberarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), bom.getCanPorUnidad() * op.getCantidad());
	    }
	    
	    op.setEstado("cancelada");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "cancelada", responsable);
	}
	
	//Falta implementar registro de material
	public void inactivarOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(!op.getEstado().equals("activa")) {
	    	return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for(Bom bom : listaMateriales) {
	    	stockAlmacenService.liberarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), bom.getCanPorUnidad() * op.getCantidad());
	    }
	    
	    op.setEstado("inactiva");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "inactiva", responsable);
	}
	

}
