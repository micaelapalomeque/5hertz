package com.proyecto_final._hertz.service;

import com.proyecto_final.model.CentroProduccion;
import com.proyecto_final.repository.CentroProduccionRepository;
import com.proyecto_final.service.CentroProduccionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CentroProduccionServiceTest {

    @Mock
    private CentroProduccionRepository repo;

    @InjectMocks
    private CentroProduccionService service;

    @Test
    void crearCentro_DatosInvalidos() {
        assertFalse(service.crearCentro("", "Desc"));
        assertFalse(service.crearCentro("Sucursal", ""));
        verify(repo, never()).save(any());
    }

    @Test
    void crearCentro_OK() {
        boolean ok = service.crearCentro("Sucursal Norte", "Descripción");
        assertTrue(ok);
        verify(repo).save(any(CentroProduccion.class));
    }

    @Test
    void obtenerTodos() {
        when(repo.findAll()).thenReturn(List.of(new CentroProduccion(), new CentroProduccion()));
        List<CentroProduccion> lista = service.obtenerTodos();
        assertEquals(2, lista.size());
    }
}
