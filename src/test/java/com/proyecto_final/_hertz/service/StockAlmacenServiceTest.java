package com.proyecto_final._hertz.service;

import com.proyecto_final.model.StockAlmacen;
import com.proyecto_final.repository.StockAlmacenRepository;
import com.proyecto_final.service.MovimientoStockService;
import com.proyecto_final.service.StockAlmacenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StockAlmacenServiceTest {

    @Mock
    private MovimientoStockService movimientoStockService;

    @Mock
    private StockAlmacenRepository stockRepo;

    @InjectMocks
    private StockAlmacenService service;

    @Test
    void habilitarProducto_CreaNuevo() {
        when(stockRepo.findBySku("A")).thenReturn(Optional.empty());

        boolean ok = service.habilitarProducto(1, "A");

        assertTrue(ok);
        verify(stockRepo).save(any(StockAlmacen.class));
    }

    @Test
    void habilitarProducto_Existe_NoHaceNada() {
        when(stockRepo.findBySku("A"))
                .thenReturn(Optional.of(new StockAlmacen()));

        boolean ok = service.habilitarProducto(1, "A");

        assertFalse(ok);
        verify(stockRepo, never()).save(any());
    }

    @Test
    void consultarStockProducto_Encontrado() {
        StockAlmacen s = new StockAlmacen(1, "A", 0, 0, 10, 10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        StockAlmacen result = service.consultarStockProducto("A",1);

        assertNotNull(result);
        assertEquals(10, result.getStockDisponible());
    }

    @Test
    void consultarStockProducto_NoExiste() {
        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.empty());

        StockAlmacen result = service.consultarStockProducto("A",1);

        assertNull(result);
    }

    @Test
    void ingresarMaterial_OK() {
        StockAlmacen s = new StockAlmacen(1,"A",0,0,10,10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.ingresarMaterial("A",1,5);

        assertTrue(ok);
        assertEquals(15, s.getStockDisponible());
        assertEquals(15, s.getStockTotal());
        verify(stockRepo).save(s);
        verify(movimientoStockService).registrarMovimiento(1,"A",5,"INGRESO");
    }

    @Test
    void ingresarMaterial_NoExiste() {
        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.empty());

        boolean ok = service.ingresarMaterial("A",1,5);

        assertFalse(ok);
        verifyNoInteractions(movimientoStockService);
    }

    @Test
    void retirarMaterial_OK() {
        StockAlmacen s = new StockAlmacen(1,"A",0,0,10,10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.retirarMaterial("A",1,5);

        assertTrue(ok);
        assertEquals(5, s.getStockDisponible());
        assertEquals(5, s.getStockTotal());
        verify(stockRepo).save(s);
        verify(movimientoStockService)
                .registrarMovimiento(1,"A",5,"RETIRO");
    }

    @Test
    void retirarMaterial_NoHayStock() {
        StockAlmacen s = new StockAlmacen(1,"A",0,0,5,5);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.retirarMaterial("A",1,10);

        assertFalse(ok);
        verify(stockRepo, never()).save(any());
    }

    @Test
    void reservarMaterial_OK() {
        StockAlmacen s = new StockAlmacen(1,"A",0,0,10,10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.reservarMaterial("A",1,5);

        assertTrue(ok);
        assertEquals(5, s.getStockDisponible());
        assertEquals(5, s.getStockReservado());
        verify(stockRepo).save(s);
        verify(movimientoStockService)
                .registrarMovimiento(1,"A",5,"RESERVA");
    }

    @Test
    void reservarMaterial_NoHayDisponible() {
        StockAlmacen s = new StockAlmacen(1,"A",0,0,3,3);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.reservarMaterial("A",1,5);

        assertFalse(ok);
        verify(stockRepo, never()).save(any());
    }

    @Test
    void liberarMaterial_OK() {
        StockAlmacen s = new StockAlmacen(1,"A",0,5,5,10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.liberarMaterial("A",1,3);

        assertTrue(ok);
        assertEquals(2, s.getStockReservado());
        assertEquals(8, s.getStockDisponible());
        verify(stockRepo).save(s);
        verify(movimientoStockService)
                .registrarMovimiento(1,"A",3,"LIBERACION");
    }

    @Test
    void liberarMaterial_NoAlcanzaReservado() {
        StockAlmacen s = new StockAlmacen(1,"A",0,2,5,7);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.liberarMaterial("A",1,5);

        assertFalse(ok);
        verify(stockRepo, never()).save(any());
    }

    @Test
    void consumirMaterial_OK() {
        StockAlmacen s = new StockAlmacen(1,"A",0,5,5,10);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.consumirMaterial("A",1,5,"CONSUMO");

        assertTrue(ok);
        assertEquals(0, s.getStockReservado());
        assertEquals(0, s.getStockDisponible());
        assertEquals(5, s.getStockTotal());
        verify(stockRepo).save(s);
        verify(movimientoStockService)
                .registrarMovimiento(1,"A",5,"CONSUMO");
    }

    @Test
    void consumirMaterial_NoAlcanzaReservado() {
        StockAlmacen s = new StockAlmacen(1,"A",0,2,5,7);

        when(stockRepo.findBySkuAndIdAlmacen("A",1))
                .thenReturn(Optional.of(s));

        boolean ok = service.consumirMaterial("A",1,5,"CONSUMO");

        assertFalse(ok);
        verify(stockRepo, never()).save(any());
    }
}
