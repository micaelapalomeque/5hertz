package com.proyecto_final._hertz.service;

import com.proyecto_final.model.Bom;
import com.proyecto_final.model.Producto;
import com.proyecto_final.repository.BomRepository;
import com.proyecto_final.repository.ProductoRepository;
import com.proyecto_final.service.BomService;
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
class BomServiceTest {

    @Mock
    private BomRepository bomRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private BomService bomService;

    @Test
    void crearBom_DatosInvalidos() {
        assertFalse(bomService.crearBom("", "MAT1", 2));
        assertFalse(bomService.crearBom("PRD", "", 2));
        assertFalse(bomService.crearBom("PRD", "MAT1", -5));
        verifyNoInteractions(bomRepository);
    }

    @Test
    void crearBom_ProductoFinalInexistente() {
        when(productoRepository.findBySku("PRD")).thenReturn(Optional.empty());
        assertFalse(bomService.crearBom("PRD", "MAT1", 3));
        verify(productoRepository).findBySku("PRD");
    }

    @Test
    void crearBom_MaterialInexistente() {
        when(productoRepository.findBySku("PRD")).thenReturn(Optional.of(new Producto()));
        when(productoRepository.findBySku("MAT1")).thenReturn(Optional.empty());

        assertFalse(bomService.crearBom("PRD", "MAT1", 3));

        verify(productoRepository).findBySku("PRD");
        verify(productoRepository).findBySku("MAT1");
        verifyNoInteractions(bomRepository);
    }

    @Test
    void crearBom_OK() {
        when(productoRepository.findBySku("PRD")).thenReturn(Optional.of(new Producto()));
        when(productoRepository.findBySku("MAT1")).thenReturn(Optional.of(new Producto()));

        assertTrue(bomService.crearBom("PRD", "MAT1", 4));
        verify(bomRepository).save(any(Bom.class));
    }

    @Test
    void eliminarBom_OK() {
        when(bomRepository.findBySkuProductoFinal("PRD"))
                .thenReturn(List.of(new Bom()));

        assertTrue(bomService.eliminarBomProducto("PRD"));
        verify(bomRepository).deleteBySkuProductoFinal("PRD");
    }

    @Test
    void eliminarBom_Inexistente() {
        when(bomRepository.findBySkuProductoFinal("PRD"))
                .thenReturn(List.of());

        assertFalse(bomService.eliminarBomProducto("PRD"));
        verify(bomRepository, never()).deleteBySkuProductoFinal(anyString());
    }

    @Test
    void obtenerListaMateriales() {
        List<Bom> lista = List.of(new Bom(), new Bom());
        when(bomRepository.findBySkuProductoFinal("PRD")).thenReturn(lista);

        List<Bom> result = bomService.obtenerListaMateriales("PRD");

        assertEquals(2, result.size());
        verify(bomRepository).findBySkuProductoFinal("PRD");
    }

    @Test
    void obtenerProductosFabricables() {
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoRepository.findProductosConBom()).thenReturn(productos);

        List<Producto> result = bomService.obtenerProductosFabricables();

        assertEquals(2, result.size());
        verify(productoRepository).findProductosConBom();
    }
}
