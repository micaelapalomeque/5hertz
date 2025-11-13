package com.proyecto_final.service;

import java.util.HashMap;
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
	
	public boolean hayStockParaFabricar(int idAlmacen, String skuProductoFinal, int cantidad) {
		List<Bom> listaMateriales = bomService.obtenerListaMateriales(skuProductoFinal);
		for(Bom bom : listaMateriales) {
			if(!stockAlmacenService.hayStockDisponible(bom.getSkuMaterial(), idAlmacen, bom.getCanPorUnidad() * cantidad)) {
				return false;
			}
		}
		return true;
	}
	
	public HashMap<String, Integer> calcularRecursosParaFabricar(String skuProductoFinal, int cantidad) {
		List<Bom> listaMateriales = bomService.obtenerListaMateriales(skuProductoFinal);	
		HashMap<String, Integer> cantidadPorMaterial = new HashMap<String, Integer>();
		for(Bom material : listaMateriales) {
			cantidadPorMaterial.put(material.getSkuMaterial(), material.getCanPorUnidad() * cantidad);
		}
		return cantidadPorMaterial;
	}

	public void activarOp(int idOp, String responsable) {
	    System.out.println("=== ACTIVANDO ORDEN " + idOp + " ===");
	    Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) {
	        System.out.println("ERROR: Orden no encontrada");
	        return;
	    }

	    OrdenProduccion op = opt.get();
	    System.out.println("Estado actual: " + op.getEstado());
	    
	    if (!op.getEstado().equals("planificada")) {
	        System.out.println("ERROR: Estado no es planificada");
	        return;
	    }
	    
	    if (!hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad())) {
	        System.out.println("ERROR: No hay stock suficiente");
	        return;
	    }
	    
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    System.out.println("Materiales encontrados: " + listaMateriales.size());
	    
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
	    System.out.println("Creando lotes: cantidad=" + op.getCantidad() + ", tamano=" + tamanoLote);
	    loteProcesoService.crearLotesIniciales(idOp, op.getCantidad(), tamanoLote);
	    System.out.println("=== FIN ACTIVACIÓN ===");
	}
	
	// Método simple solo para cambiar estado (sin reservas ni lotes)
	public void activarOpSimple(int idOp, String responsable) {
	    System.out.println("=== ACTIVACIÓN SIMPLE ORDEN " + idOp + " ===");
	    Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) {
	        System.out.println("ERROR: Orden no encontrada");
	        return;
	    }

	    OrdenProduccion op = opt.get();
	    System.out.println("Estado actual: " + op.getEstado());
	    
	    op.setEstado("activa");
	    ordenProduccionRepository.save(op);
	    cambioOpService.registrarCambio(idOp, "activa", responsable);
	    System.out.println("Estado cambiado a activa");
	}
	
	// Cambiar estado rápidamente sin procesos pesados
	public void cambiarEstadoRapido(int idOp, String nuevoEstado, String responsable) {
	    Optional<OrdenProduccion> opt = consultarOp(idOp);
	    if (opt.isPresent()) {
	        OrdenProduccion op = opt.get();
	        op.setEstado(nuevoEstado);
	        ordenProduccionRepository.save(op);
	        cambioOpService.registrarCambio(idOp, nuevoEstado, responsable);
	    }
	}
	
	// Procesar reservas y lotes (proceso pesado)
	public void procesarReservasYLotes(int idOp, String responsable) {
	    System.out.println("=== PROCESANDO RESERVAS Y LOTES PARA ORDEN " + idOp + " ===");
	    Optional<OrdenProduccion> opt = consultarOp(idOp);
	    if (opt.isEmpty()) {
	        System.out.println("ERROR: Orden no encontrada en proceso asíncrono");
	        return;
	    }
	    
	    OrdenProduccion op = opt.get();
	    System.out.println("Procesando orden: " + op.getSku() + ", cantidad: " + op.getCantidad());
	    
	    // Reservar materiales
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    System.out.println("Materiales a reservar: " + listaMateriales.size());
	    
	    for (Bom bom : listaMateriales) {
	        stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), op.getCantidad() * bom.getCanPorUnidad());
	        reservaMaterialService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), op.getCantidad() * bom.getCanPorUnidad());
	        reservaMaterialService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), op.getCantidad() * bom.getCanPorUnidad());
	        System.out.println("Material reservado: " + bom.getSkuMaterial());
	    }
	    
	    // Crear lotes
	    System.out.println("=== CREANDO LOTES ===");
	    ConfigProduccion config = configProduccionService.obtenerConfiguracion();
	    int tamanoLote = op.getCantidad() / config.getNumeroLotesFijo();
	    System.out.println("Configuración: cantidad=" + op.getCantidad() + ", lotes=" + config.getNumeroLotesFijo() + ", tamaño=" + tamanoLote);
	    
	    loteProcesoService.crearLotesIniciales(idOp, op.getCantidad(), tamanoLote);
	    System.out.println("=== FIN PROCESO ASÍNCRONO ===");
	}
	
	public void consumirOp(int idOp, String responsable) {
		Optional<OrdenProduccion> opt = consultarOp(idOp);
	    
	    if (opt.isEmpty()) return;

	    OrdenProduccion op = opt.get();
	    
	    if(!op.getEstado().equals("activa")) {
	    	return;
	    }
	    
	    // Consumir definitivamente los materiales reservados
	    List<Bom> listaMateriales = bomService.obtenerListaMateriales(op.getSku());
	    
	    for(Bom bom : listaMateriales) {
	    	int cantidadReservada = reservaMaterialService.consultarMaterialReservado(idOp, bom.getSkuMaterial());
	    	stockAlmacenService.consumirMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidadReservada, "CONSUMO_PRODUCCION");
	    	reservaMaterialService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), 0);
	    	reservaMaterialService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), 0);
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
	    
	    // Cancelar todos los lotes de esta orden
	    loteProcesoService.cancelarLotesPorOrden(idOp);
	     
	    op.setEstado("cancelada");
    	ordenProduccionRepository.save(op);
    	cambioOpService.registrarCambio(idOp, "cancelada", responsable);
	}
	

}
