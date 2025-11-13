package com.proyecto_final._hertz.service;

import com.proyecto_final.model.CategoriaProducto;
import com.proyecto_final.repository.CategoriaProductoRepository;
import com.proyecto_final.service.CategoriaProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoriaProductoServiceTest {

    @Mock
    private CategoriaProductoRepository repo;

    @InjectMocks
    private CategoriaProductoService service;

    @Test
    void agregarCategoria_OK() {
        when(repo.findByNombre("Frutas")).thenReturn(Optional.empty());

        boolean ok = service.agregarCategoria("Frutas", "Categoria de frutas");

        assertTrue(ok);
        verify(repo).save(any(CategoriaProducto.class));
    }

    @Test
    void agregarCategoria_DatosInvalidos() {
        assertFalse(service.agregarCategoria("", "Desc"));
        assertFalse(service.agregarCategoria("Frutas", ""));
        verify(repo, never()).save(any());
    }

    @Test
    void agregarCategoria_Duplicado() {
        when(repo.findByNombre("Frutas")).thenReturn(Optional.of(new CategoriaProducto()));

        boolean ok = service.agregarCategoria("Frutas", "Descripcion");

        assertFalse(ok);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminarCategoria_OK() {
        CategoriaProducto cat = new CategoriaProducto("Frutas","Desc");
        when(repo.findByNombre("Frutas")).thenReturn(Optional.of(cat));

        boolean ok = service.eliminarCategoria("Frutas");

        assertTrue(ok);
        verify(repo).delete(cat);
    }

    @Test
    void eliminarCategoria_Inexistente() {
        when(repo.findByNombre("Frutas")).thenReturn(Optional.empty());

        boolean ok = service.eliminarCategoria("Frutas");

        assertFalse(ok);
        verify(repo, never()).delete(any());
    }

    @Test
    void findByNombre() {
        CategoriaProducto cat = new CategoriaProducto("Bebidas","Desc");
        when(repo.findByNombre("Bebidas")).thenReturn(Optional.of(cat));

        Optional<CategoriaProducto> result = service.findByNombre("Bebidas");

        assertTrue(result.isPresent());
        assertEquals("Bebidas", result.get().getNombre());
    }

    @Test
    void obtenerTodas() {
        when(repo.findAll()).thenReturn(List.of(new CategoriaProducto(), new CategoriaProducto()));

        List<CategoriaProducto> lista = service.obtenerTodas();

        assertEquals(2, lista.size());
        verify(repo).findAll();
    }
}
