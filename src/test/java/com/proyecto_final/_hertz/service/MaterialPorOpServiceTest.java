package com.proyecto_final._hertz.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.proyecto_final.model.MaterialPorOp;
import com.proyecto_final.repository.MaterialPorOpRepository;
import com.proyecto_final.service.MaterialPorOpService;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MaterialPorOpServiceTest {

    @Mock
    private MaterialPorOpRepository repo;

    @InjectMocks
    private MaterialPorOpService service;

    @Test
    void registrarReserva_OK() {
        boolean ok = service.registrarReserva(1, "MAT1", 10);
        assertTrue(ok);
        verify(repo).save(any(MaterialPorOp.class));
    }

    @Test
    void registrarReserva_Invalidos() {
        assertFalse(service.registrarReserva(0, "MAT1", 10));
        assertFalse(service.registrarReserva(1, "", 10));
        assertFalse(service.registrarReserva(1, "MAT1", -5));
    }

    @Test
    void consultarReservas() {
        when(repo.findAll()).thenReturn(List.of(new MaterialPorOp(), new MaterialPorOp()));
        List<MaterialPorOp> lista = service.consultarReservas();
        assertEquals(2, lista.size());
    }

    @Test
    void consultarReservasPorOp() {
        when(repo.findByIdOp(5)).thenReturn(List.of(new MaterialPorOp()));
        assertEquals(1, service.consultarReservasPorOp(5).size());
    }

    @Test
    void consultarMaterialReservado() {
        MaterialPorOp m = new MaterialPorOp(5, "MAT1", 20);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));
        assertEquals(20, service.consultarMaterialReservado(5,"MAT1"));
    }

    @Test
    void consultarMaterialConsumido() {
        MaterialPorOp m = new MaterialPorOp(5, "MAT1", 20);
        m.setCantidadConsumida(7);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));
        assertEquals(7, service.consultarMaterialConsumido(5,"MAT1"));
    }

    @Test
    void consultarDiferencia() {
        MaterialPorOp m = new MaterialPorOp(5,"MAT1",20);
        m.setCantidadConsumida(8);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));
        assertEquals(12, service.consultarDiferencia(5,"MAT1"));
    }

    @Test
    void consultarCantidadPendiente() {
        MaterialPorOp m = new MaterialPorOp(5,"MAT1",20);
        m.setCantidadPendiente(9);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));
        assertEquals(9, service.consultarCantidadPendiente(5,"MAT1"));
    }

    @Test
    void modificarCantidadReservada_OK() {
        MaterialPorOp m = new MaterialPorOp(5,"MAT1",20);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));

        boolean ok = service.modificarCantidadReservada(5,"MAT1",15);

        assertTrue(ok);
        assertEquals(15, m.getCantidadReservada());
        verify(repo).save(m);
    }

    @Test
    void modificarCantidadConsumida_OK() {
        MaterialPorOp m = new MaterialPorOp(5,"MAT1",20);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));

        boolean ok = service.modificarCantidadConsumida(5,"MAT1",7);

        assertTrue(ok);
        assertEquals(7, m.getCantidadConsumida());
        verify(repo).save(m);
    }

    @Test
    void modificarCantidadPendiente_OK() {
        MaterialPorOp m = new MaterialPorOp(5,"MAT1",20);
        when(repo.findByIdOpAndSku(5,"MAT1")).thenReturn(Optional.of(m));

        boolean ok = service.modificarCantidadPendiente(5,"MAT1",12);

        assertTrue(ok);
        assertEquals(12, m.getCantidadPendiente());
        verify(repo).save(m);
    }
}
