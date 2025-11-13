package com.proyecto_final._hertz.service;

import com.proyecto_final.model.MovimientoStock;
import com.proyecto_final.repository.MovimientoStockRepository;
import com.proyecto_final.service.MovimientoStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MovimientoStockServiceTest {

    @Mock
    private MovimientoStockRepository repo;

    @InjectMocks
    private MovimientoStockService service;

    @Test
    void registrarMovimiento_DatosInvalidos() {
        assertFalse(service.registrarMovimiento(0, "SKU1", 10, "INGRESO"));
        assertFalse(service.registrarMovimiento(1, "", 10, "INGRESO"));
        assertFalse(service.registrarMovimiento(1, "SKU1", 0, "INGRESO"));
        assertFalse(service.registrarMovimiento(1, "SKU1", 10, ""));
        verify(repo, never()).save(any());
    }

    @Test
    void registrarMovimiento_OK_NormalizaTipo() {
        boolean ok = service.registrarMovimiento(1, "SKU1", 10, "ingreso");

        assertTrue(ok);
        verify(repo).save(argThat(m ->
                m.getIdAlmacen() == 1 &&
                m.getSku().equals("SKU1") &&
                m.getCantidad() == 10 &&
                m.getTipoMovimiento().equals("INGRESO")
        ));
    }

    @Test
    void consultarEgresos() {
        List<MovimientoStock> lista = List.of(new MovimientoStock(), new MovimientoStock());
        when(repo.findByTipoMovimiento("EGRESO")).thenReturn(lista);

        List<MovimientoStock> result = service.consultarEgresos();

        assertEquals(2, result.size());
        verify(repo).findByTipoMovimiento("EGRESO");
    }

    @Test
    void consultarIngresos() {
        List<MovimientoStock> lista = List.of(new MovimientoStock());
        when(repo.findByTipoMovimiento("INGRESO")).thenReturn(lista);

        List<MovimientoStock> result = service.consultarIngresos();

        assertEquals(1, result.size());
        verify(repo).findByTipoMovimiento("INGRESO");
    }

    @Test
    void consultarPorSku_SkuInvalido() {
        List<MovimientoStock> r1 = service.consultarPorSku(null);
        List<MovimientoStock> r2 = service.consultarPorSku("");

        assertTrue(r1.isEmpty());
        assertTrue(r2.isEmpty());
        verify(repo, never()).findBySku(anyString());
    }

    @Test
    void consultarPorSku_OK() {
        List<MovimientoStock> lista = List.of(new MovimientoStock(), new MovimientoStock());
        when(repo.findBySku("SKU1")).thenReturn(lista);

        List<MovimientoStock> result = service.consultarPorSku("SKU1");

        assertEquals(2, result.size());
        verify(repo).findBySku("SKU1");
    }

    @Test
    void consultarTodos() {
        when(repo.findAll()).thenReturn(List.of(new MovimientoStock(), new MovimientoStock(), new MovimientoStock()));

        List<MovimientoStock> result = service.consultarTodos();

        assertEquals(3, result.size());
        verify(repo).findAll();
    }
}
