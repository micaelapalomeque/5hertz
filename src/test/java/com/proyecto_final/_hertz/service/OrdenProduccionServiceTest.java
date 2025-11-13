package com.proyecto_final._hertz.service;

import com.proyecto_final.model.*;
import com.proyecto_final.repository.OrdenProduccionRepository;
import com.proyecto_final.service.BomService;
import com.proyecto_final.service.CambioOpService;
import com.proyecto_final.service.ConfigProduccionService;
import com.proyecto_final.service.LoteProcesoService;
import com.proyecto_final.service.MaterialPorOpService;
import com.proyecto_final.service.OrdenProduccionService;
import com.proyecto_final.service.StockAlmacenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class OrdenProduccionServiceTest {

    @Mock private OrdenProduccionRepository repo;
    @Mock private CambioOpService cambioOp;
    @Mock private StockAlmacenService stock;
    @Mock private MaterialPorOpService materiales;
    @Mock private BomService bomService;
    @Mock private LoteProcesoService lotes;
    @Mock private ConfigProduccionService configService;

    @InjectMocks
    private OrdenProduccionService service;

    @Test
    void crearOp_OK() {
        Bom b1 = new Bom();
        b1.setSkuMaterial("MAT1");

        when(bomService.obtenerListaMateriales("PRD"))
                .thenReturn(List.of(b1));

        service.crearOp(1,"PRD",10,"Juan");

        verify(repo).save(any(OrdenProduccion.class));
        verify(cambioOp).registrarCambio(anyInt(), eq("planificada"), eq("Juan"));
        verify(materiales).registrarReserva(anyInt(), eq("MAT1"), eq(0));
    }

    @Test
    void hayStockParaFabricar_OK() {
        Bom bom = new Bom();
        bom.setSkuMaterial("MAT1");
        bom.setCanPorUnidad(2);

        when(bomService.obtenerListaMateriales("PRD"))
                .thenReturn(List.of(bom));

        when(stock.hayStockDisponible("MAT1",1,20))
                .thenReturn(true);

        assertTrue(service.hayStockParaFabricar(1,"PRD",10));
    }

    @Test
    void hayStockParaFabricar_NoAlcanza() {
        Bom bom = new Bom();
        bom.setSkuMaterial("MAT1");
        bom.setCanPorUnidad(2);

        when(bomService.obtenerListaMateriales("PRD"))
                .thenReturn(List.of(bom));

        when(stock.hayStockDisponible("MAT1",1,20))
                .thenReturn(false);

        assertFalse(service.hayStockParaFabricar(1,"PRD",10));
    }

    @Test
    void activarOp_FlujoCompleto() {
        OrdenProduccion op = new OrdenProduccion(1,"PRD",10,"planificada","Juan");
        op.setIdOp(5);

        Bom bom = new Bom();
        bom.setSkuMaterial("MAT1");
        bom.setCanPorUnidad(2);

        when(repo.findById(5)).thenReturn(Optional.of(op));
        when(bomService.obtenerListaMateriales("PRD")).thenReturn(List.of(bom));
        when(stock.hayStockDisponible("MAT1",1,20)).thenReturn(true);

        ConfigProduccion cfg = new ConfigProduccion();
        cfg.setNumeroLotesFijo(2);
        when(configService.obtenerConfiguracion()).thenReturn(cfg);

        boolean ok = service.activarOp(5,"Juan");

        assertTrue(ok);
        verify(stock).reservarMaterial("MAT1",1,20);
        verify(materiales).modificarCantidadReservada(5,"MAT1",20);
        verify(materiales).modificarCantidadPendiente(5,"MAT1",20);
        verify(repo).save(op);
        verify(cambioOp).registrarCambio(5,"activa","Juan");
        verify(lotes).crearLotesIniciales(5,10,5);
    }

    @Test
    void pausarOp_OK() {
        OrdenProduccion op = new OrdenProduccion(1,"PRD",10,"activa","Juan");
        op.setIdOp(7);

        Bom b = new Bom();
        b.setSkuMaterial("MAT1");

        when(repo.findById(7)).thenReturn(Optional.of(op));
        when(bomService.obtenerListaMateriales("PRD")).thenReturn(List.of(b));
        when(materiales.consultarDiferencia(7,"MAT1")).thenReturn(5);

        boolean ok = service.pausarOp(7,"Juan");

        assertTrue(ok);
        verify(stock).liberarMaterial("MAT1",1,5);
        verify(materiales).modificarCantidadReservada(7,"MAT1",0);
        verify(repo).save(op);
        verify(cambioOp).registrarCambio(7,"pausada","Juan");
    }

    @Test
    void reanudarOp_OK() {
        OrdenProduccion op = new OrdenProduccion(1, "PRD", 10, "pausada", "Juan");
        op.setIdOp(9);

        Bom bom = new Bom();
        bom.setSkuMaterial("MAT1");
        bom.setCanPorUnidad(2);

        when(repo.findById(9)).thenReturn(Optional.of(op));
        when(bomService.obtenerListaMateriales("PRD")).thenReturn(List.of(bom));
        when(materiales.consultarCantidadPendiente(9, "MAT1")).thenReturn(8);

        // *** IMPORTANTE ***
        // hayStockParaFabricar llama a stock.hayStockDisponible(...)
        when(stock.hayStockDisponible("MAT1", 1, 20)).thenReturn(true);

        boolean ok = service.reanudarOp(9, "Juan");

        assertTrue(ok);

        verify(stock).reservarMaterial("MAT1", 1, 8);
        verify(materiales).modificarCantidadReservada(9, "MAT1", 8);
        verify(repo).save(op);
        verify(cambioOp).registrarCambio(9, "reanudada", "Juan");
    }

    @Test
    void consumirOp_OK() {
        OrdenProduccion op = new OrdenProduccion(1,"PRD",10,"activa","Juan");
        op.setIdOp(4);

        Bom bom = new Bom();
        bom.setSkuMaterial("MAT1");

        when(repo.findById(4)).thenReturn(Optional.of(op));
        when(bomService.obtenerListaMateriales("PRD")).thenReturn(List.of(bom));
        when(materiales.consultarMaterialReservado(4,"MAT1")).thenReturn(10);

        boolean ok = service.consumirOp(4,"Juan");

        assertTrue(ok);
        verify(stock).consumirMaterial("MAT1",1,10,"CONSUMO_PRODUCCION");
        verify(materiales).modificarCantidadReservada(4,"MAT1",0);
        verify(materiales).modificarCantidadPendiente(4,"MAT1",0);
        verify(cambioOp).registrarCambio(4,"consumida","Juan");
    }

    @Test
    void cancelarOp_OK() {
        OrdenProduccion op = new OrdenProduccion(1,"PRD",10,"planificada","Juan");
        op.setIdOp(6);

        when(repo.findById(6)).thenReturn(Optional.of(op));

        boolean ok = service.cancelarOp(6,"Juan");

        assertTrue(ok);
        verify(repo).save(op);
        verify(cambioOp).registrarCambio(6,"cancelada","Juan");
    }

    @Test
    void calcularRecursos() {
        Bom b1 = new Bom();
        b1.setSkuMaterial("MAT1");
        b1.setCanPorUnidad(2);

        Bom b2 = new Bom();
        b2.setSkuMaterial("MAT2");
        b2.setCanPorUnidad(3);

        when(bomService.obtenerListaMateriales("PRD"))
                .thenReturn(List.of(b1,b2));

        Map<String,Integer> map = service.calcularRecursosParaFabricar("PRD",10);

        assertEquals(20, map.get("MAT1"));
        assertEquals(30, map.get("MAT2"));
    }
}
