package com.proyecto_final.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.repository.OrdenProduccionRepository;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.model.MaterialPorOp;
import com.proyecto_final.model.Bom;
import com.proyecto_final.model.ConfigProduccion;

@Service
public class OrdenProduccionService {
	
	private CambioOpService cambioOpService;
	private StockAlmacenService stockAlmacenService;
	private MaterialPorOpService reservaMaterialService;
	private BomService bomService;
	private LoteProcesoService loteProcesoService;
	private ConfigProduccionService configProduccionService;
	private OrdenProduccionRepository ordenProduccionRepository;

	public OrdenProduccionService(OrdenProduccionRepository ordenProduccionRepository, 
			CambioOpService cambioOpService, StockAlmacenService stockAlmacenService, BomService bomService, 
			MaterialPorOpService reservaMaterialService, LoteProcesoService loteProcesoService,
			ConfigProduccionService configProduccionService) {
		this.ordenProduccionRepository = ordenProduccionRepository;
		this.reservaMaterialService = reservaMaterialService;
		this.cambioOpService = cambioOpService;
		this.stockAlmacenService = stockAlmacenService;
		this.bomService = bomService;
		this.loteProcesoService = loteProcesoService;
		this.configProduccionService = configProduccionService;
	}

	public void crearOp(int idAlmacen, String sku, int cantidad, String responsable) {
		OrdenProduccion op = new OrdenProduccion(idAlmacen, sku, cantidad, "planificada", responsable);
		ordenProduccionRepository.save(op);
		cambioOpService.registrarCambio(op.getIdOp(), "planificada", responsable);
		
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for (Bom bom : listaMateriales) {
	        reservaMaterialService.registrarReserva(op.getIdOp(), bom.getSkuMaterial(), 0);
	    }
	}
	
	public Optional<OrdenProduccion> consultarOp(int idOp) {
		return ordenProduccionRepository.findById(idOp);
	}
	
	public List<OrdenProduccion> consultarTodas() {
		return ordenProduccionRepository.findAll();
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
	    
	    if (!op.getEstado().equals("planificada")) return;
	    if (!hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad())) return;
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for (Bom bom : listaMateriales) {
	        stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), op.getCantidad() * bom.getCanPorUnidad());
	        reservaMaterialService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), op.getCantidad() * bom.getCanPorUnidad());
	        reservaMaterialService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), op.getCantidad() * bom.getCanPorUnidad());
	    }
	    
	    op.setEstado("activa");
	    ordenProduccionRepository.save(op);
	    cambioOpService.registrarCambio(idOp, "activa", responsable);
	    
	    // Crear lotes iniciales usando configuración
	    ConfigProduccion config = configProduccionService.obtenerConfiguracion();
	    int tamanoLote = op.getCantidad() / config.getNumeroLotesFijo();
	    loteProcesoService.crearLotesIniciales(idOp, op.getCantidad(), tamanoLote);
	}
	
	public void consumirOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(!op.getEstado().equals("activa")) {
	    	return;
	    }
	    
	    op.setEstado("consumida");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "consumida", responsable);
	}
	
	public void pausarOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(!op.getEstado().equals("activa") && !op.getEstado().equals("reanudada")) {
	    	return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for(Bom bom : listaMateriales) {
	    	int cantidadAliberar = reservaMaterialService.consultarDiferencia(idOp, bom.getSkuMaterial());
	    	stockAlmacenService.liberarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidadAliberar);
	    	reservaMaterialService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), 0);
	    }
	    
	    op.setEstado("pausada");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "pausada", responsable);
	}
	
	public void reanudarOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if (!op.getEstado().equals("pausada")) return;
	    if (!hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad())) return;
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for (Bom bom : listaMateriales) {
	    	int cantidadPendiente = reservaMaterialService.consultarCantidadPendiente(idOp, bom.getSkuMaterial());
	    	stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidadPendiente);
	    	reservaMaterialService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), cantidadPendiente);
	    }
	    
	    op.setEstado("reanudada");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "reanudada", responsable);    
	}
	
	public void cancelarOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(op.getEstado().equals("consumida") || op.getEstado().equals("cancelada")) {
	    	return;
	    }
	    
	    if(!op.getEstado().equals("pausada")) {
	    	pausarOp(idOp, responsable);
	    }	
	     
	    op.setEstado("cancelada");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "cancelada", responsable);
	}
	

}
