package com.proyecto_final._hertz.service;

import com.proyecto_final.model.Almacen;
import com.proyecto_final.repository.AlmacenRepository;
import com.proyecto_final.service.AlmacenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AlmacenServiceTest {

    @Mock
    private AlmacenRepository repo;

    @InjectMocks
    private AlmacenService service;

    @Test
    void crearAlmacen_DatosInvalidos() {
        assertFalse(service.crearAlmacen(0, "Depósito", 50, "Activo"));
        assertFalse(service.crearAlmacen(1, "", 50, "Activo"));
        assertFalse(service.crearAlmacen(1, "Depósito", -10, "Activo"));
        assertFalse(service.crearAlmacen(1, "Depósito", 50, ""));
        verify(repo, never()).save(any());
    }

    @Test
    void crearAlmacen_OK() {
        boolean ok = service.crearAlmacen(1, "Depósito Central", 100, "Activo");
        assertTrue(ok);
        verify(repo).save(any(Almacen.class));
    }

    @Test
    void obtenerTodos() {
        when(repo.findAll()).thenReturn(List.of(new Almacen(), new Almacen()));
        List<Almacen> lista = service.obtenerTodos();
        assertEquals(2, lista.size());
        verify(repo).findAll();
    }
}
