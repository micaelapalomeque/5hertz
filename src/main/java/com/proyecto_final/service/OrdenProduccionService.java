package com.proyecto_final.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.proyecto_final.model.Bom;
import com.proyecto_final.model.ConfigProduccion;
import com.proyecto_final.model.OrdenProduccion;
import com.proyecto_final.repository.OrdenProduccionRepository;
import com.proyecto_final.service.MaterialPorOrdenService;

@Service
public class OrdenProduccionService {

    private final CambioOpService cambioOpService;
    private final StockAlmacenService stockAlmacenService;
    private final MaterialPorOpService materialPorOpService;
    private final MaterialPorOrdenService materialPorOrdenService;
    private final BomService bomService;
    private final LoteProcesoService loteProcesoService;
    private final ConfigProduccionService configProduccionService;
    private final OrdenProduccionRepository ordenProduccionRepository;

    public OrdenProduccionService(
            OrdenProduccionRepository ordenProduccionRepository,
            CambioOpService cambioOpService,
            StockAlmacenService stockAlmacenService,
            BomService bomService,
            MaterialPorOpService materialPorOpService,
            MaterialPorOrdenService materialPorOrdenService,
            LoteProcesoService loteProcesoService,
            ConfigProduccionService configProduccionService) {

        this.ordenProduccionRepository = ordenProduccionRepository;
        this.cambioOpService = cambioOpService;
        this.stockAlmacenService = stockAlmacenService;
        this.bomService = bomService;
        this.materialPorOpService = materialPorOpService;
        this.materialPorOrdenService = materialPorOrdenService;
        this.loteProcesoService = loteProcesoService;
        this.configProduccionService = configProduccionService;
    }

    private boolean datosInvalidosOp(int idAlmacen, String sku, int cantidad, String responsable) {
        if (idAlmacen <= 0) return true;
        if (sku == null || sku.isBlank()) return true;
        if (cantidad <= 0) return true;
        if (responsable == null || responsable.isBlank()) return true;
        return false;
    }

    private Optional<OrdenProduccion> obtenerOp(int idOp) {
        if (idOp <= 0) return Optional.empty();
        return ordenProduccionRepository.findById(idOp);
    }

    private boolean esEstado(OrdenProduccion op, String estado) {
        return estado != null && estado.equalsIgnoreCase(op.getEstado());
    }

    public boolean crearOp(int idAlmacen, String sku, int cantidad, String responsable) {
        if (datosInvalidosOp(idAlmacen, sku, cantidad, responsable)) {
            return false;
        }

        OrdenProduccion op = new OrdenProduccion(idAlmacen, sku, cantidad, "planificada", responsable);
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(op.getIdOp(), "planificada", responsable);

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            // Crear registro en reserva_material (para reservas de stock)
            materialPorOpService.registrarReserva(op.getIdOp(), bom.getSkuMaterial(), 0);
            // Crear registro en material_por_op (para desperdicio) - usar cantidad calculada
            int cantidadNecesaria = op.getCantidad() * bom.getCanPorUnidad();
            materialPorOrdenService.crearRegistroInicial(op.getIdOp(), bom.getSkuMaterial(), cantidadNecesaria);
        }

        return true;
    }

    public Optional<OrdenProduccion> consultarOp(int idOp) {
        return obtenerOp(idOp);
    }

    public List<OrdenProduccion> consultarTodas() {
        return ordenProduccionRepository.findAll();
    }

    public boolean hayStockParaFabricar(int idAlmacen, String skuProductoFinal, int cantidad) {
        if (idAlmacen <= 0 || skuProductoFinal == null || skuProductoFinal.isBlank() || cantidad <= 0) {
            return false;
        }

        List<Bom> materiales = bomService.obtenerListaMateriales(skuProductoFinal);
        for (Bom bom : materiales) {
            int cantidadNecesaria = bom.getCanPorUnidad() * cantidad;
            boolean hayStock = stockAlmacenService.hayStockDisponible(bom.getSkuMaterial(), idAlmacen, cantidadNecesaria);
            if (!hayStock) {
                return false;
            }
        }

        return true;
    }

    public Map<String, Integer> calcularRecursosParaFabricar(String skuProductoFinal, int cantidad) {
        if (skuProductoFinal == null || skuProductoFinal.isBlank() || cantidad <= 0) {
            return Map.of();
        }

        List<Bom> materiales = bomService.obtenerListaMateriales(skuProductoFinal);
        Map<String, Integer> cantidadPorMaterial = new HashMap<>();

        for (Bom material : materiales) {
            int total = material.getCanPorUnidad() * cantidad;
            cantidadPorMaterial.put(material.getSkuMaterial(), total);
        }

        return cantidadPorMaterial;
    }

    public boolean activarOp(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;

        OrdenProduccion op = opt.get();
        if (!esEstado(op, "planificada")) return false;
        if (responsable == null || responsable.isBlank()) return false;

        boolean hayStock = hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad());
        if (!hayStock) return false;

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            int cantidad = op.getCantidad() * bom.getCanPorUnidad();
            stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidad);
            materialPorOpService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), cantidad);
            materialPorOpService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), cantidad);
        }

        op.setEstado("activa");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "activa", responsable);

        ConfigProduccion config = configProduccionService.obtenerConfiguracion();
        if (config != null && config.getNumeroLotesFijo() > 0) {
            int tamanoLote = op.getCantidad() / config.getNumeroLotesFijo();
            if (tamanoLote <= 0) {
                tamanoLote = op.getCantidad();
            }
            loteProcesoService.crearLotesIniciales(idOp, op.getCantidad(), tamanoLote);
        }

        return true;
    }

    public boolean activarOpSimple(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();
        op.setEstado("activa");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "activa", responsable);

        return true;
    }

    public boolean cambiarEstadoRapido(int idOp, String nuevoEstado, String responsable) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;

        OrdenProduccion op = opt.get();
        op.setEstado(nuevoEstado);
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, nuevoEstado, responsable);

        return true;
    }

    public boolean procesarReservasYLotes(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            int cantidad = op.getCantidad() * bom.getCanPorUnidad();
            stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidad);
            materialPorOpService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), cantidad);
            materialPorOpService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), cantidad);
        }

        ConfigProduccion config = configProduccionService.obtenerConfiguracion();
        if (config != null && config.getNumeroLotesFijo() > 0) {
            int tamanoLote = op.getCantidad() / config.getNumeroLotesFijo();
            if (tamanoLote <= 0) {
                tamanoLote = op.getCantidad();
            }
            loteProcesoService.crearLotesIniciales(idOp, op.getCantidad(), tamanoLote);
        }

        return true;
    }

    public boolean consumirOp(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();
        if (!esEstado(op, "activa") && !esEstado(op, "reanudada")) return false;

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            int cantidadReservada = materialPorOpService.consultarMaterialReservado(idOp, bom.getSkuMaterial());
            if (cantidadReservada > 0) {
                stockAlmacenService.consumirMaterial(
                        bom.getSkuMaterial(),
                        op.getIdAlmacen(),
                        cantidadReservada,
                        responsable
                );
                materialPorOpService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), 0);
                materialPorOpService.modificarCantidadPendiente(idOp, bom.getSkuMaterial(), 0);
            }
        }

        op.setEstado("consumida");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "consumida", responsable);

        return true;
    }

    public boolean pausarOp(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();
        if (!esEstado(op, "activa") && !esEstado(op, "reanudada")) return false;

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            int cantidadALiberar = materialPorOpService.consultarDiferencia(idOp, bom.getSkuMaterial());
            if (cantidadALiberar > 0) {
                stockAlmacenService.liberarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidadALiberar);
                materialPorOpService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), 0);
            }
        }

        op.setEstado("pausada");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "pausada", responsable);

        return true;
    }

    public boolean reanudarOp(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();
        if (!esEstado(op, "pausada")) return false;

        boolean hayStock = hayStockParaFabricar(op.getIdAlmacen(), op.getSku(), op.getCantidad());
        if (!hayStock) return false;

        List<Bom> materiales = bomService.obtenerListaMateriales(op.getSku());
        for (Bom bom : materiales) {
            int cantidadPendiente = materialPorOpService.consultarCantidadPendiente(idOp, bom.getSkuMaterial());
            if (cantidadPendiente > 0) {
                stockAlmacenService.reservarMaterial(bom.getSkuMaterial(), op.getIdAlmacen(), cantidadPendiente);
                materialPorOpService.modificarCantidadReservada(idOp, bom.getSkuMaterial(), cantidadPendiente);
            }
        }

        op.setEstado("reanudada");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "reanudada", responsable);

        return true;
    }

    public boolean cancelarOp(int idOp, String responsable) {
        Optional<OrdenProduccion> opt = obtenerOp(idOp);
        if (!opt.isPresent()) return false;
        if (responsable == null || responsable.isBlank()) return false;

        OrdenProduccion op = opt.get();

        if (esEstado(op, "consumida") || esEstado(op, "cancelada")) {
            return false;
        }

        if (!esEstado(op, "pausada")) {
            pausarOp(idOp, responsable);
        }

        // Cancelar todos los lotes de esta orden
        loteProcesoService.cancelarLotesPorOrden(idOp);
        
        // Liberar materiales de material_por_op (eliminar registros)
        materialPorOrdenService.liberarMaterialesPorOrden(idOp);

        op.setEstado("cancelada");
        ordenProduccionRepository.save(op);
        cambioOpService.registrarCambio(idOp, "cancelada", responsable);

        return true;
    }
}
