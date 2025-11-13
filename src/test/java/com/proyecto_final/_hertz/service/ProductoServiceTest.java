package com.proyecto_final._hertz.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.ProductoRepository;
import com.proyecto_final.service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @Test
    void testAltaProducto_DatosInvalidos() {
        Producto p = new Producto(null,1,null,null,null);
        assertFalse(service.altaProducto(p));
    }

    @Test
    void testAltaProducto_Duplicado() {
        when(repo.findBySku("A")).thenReturn(Optional.of(new Producto()));
        Producto p = new Producto("A",1,"X","kg","desc");
        assertFalse(service.altaProducto(p));
    }

    @Test
    void testAltaProducto_Exito() {
        when(repo.findBySku("A")).thenReturn(Optional.empty());
        Producto p = new Producto("A",1,"X","kg","desc");
        assertTrue(service.altaProducto(p));
        verify(repo).save(p);
    }

    @Test
    void testBajaProducto_Inexistente() {
        when(repo.findBySku("X")).thenReturn(Optional.empty());
        assertFalse(service.bajaProducto("X"));
    }
}
