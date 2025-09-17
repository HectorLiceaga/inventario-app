package com.almacenbelier.inventarioApp;

import com.almacenbelier.inventarioApp.dto.ProductoDTO;
import com.almacenbelier.inventarioApp.model.Categoria;
import com.almacenbelier.inventarioApp.model.Marca;
import com.almacenbelier.inventarioApp.model.Producto;
import com.almacenbelier.inventarioApp.repository.CategoriaRepository;
import com.almacenbelier.inventarioApp.repository.MarcaRepository;
import com.almacenbelier.inventarioApp.repository.ProductoRepository;
import com.almacenbelier.inventarioApp.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    // 1. Mockeamos TODAS las dependencias del servicio
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private MarcaRepository marcaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    // Ya no usamos @InjectMocks
    private ProductoService productoService;

    // 2. Este método se ejecuta antes de cada test
    @BeforeEach
    void setUp() {
        // Creamos la instancia del servicio manualmente, pasando los mocks en el constructor.
        // ¡Esto es posible gracias a la inyección por constructor!
        productoService = new ProductoService(productoRepository, marcaRepository, categoriaRepository);
    }

    @Test
    void deberiaCrearUnProductoNuevo() {
        // --- ARRANGE (Preparar el escenario) ---

        // a. Creamos el DTO que simula la entrada desde el controlador
        ProductoDTO productoDTO_entrada = new ProductoDTO();
        productoDTO_entrada.setSku("BUZO-ROJO-S");
        productoDTO_entrada.setNombre("Buzo Rojo Talle S");
        productoDTO_entrada.setMarcaId(1L);
        productoDTO_entrada.setCategoriaId(10L);
        // ... otros campos del DTO

        // b. Creamos las entidades que los repositorios "encontrarán"
        Marca marcaEncontrada = new Marca();
        marcaEncontrada.setId(1L);
        marcaEncontrada.setNombre("Marca Famosa");

        Categoria categoriaEncontrada = new Categoria();
        categoriaEncontrada.setId(10L);
        categoriaEncontrada.setNombre("Buzos");

        // c. Creamos la entidad Producto que el repositorio "guardará"
        Producto productoGuardado = new Producto();
        productoGuardado.setId(99L); // Simulamos que la BD le asignó un ID
        productoGuardado.setSku("BUZO-ROJO-S");
        productoGuardado.setNombre("Buzo Rojo Talle S");
        productoGuardado.setMarca(marcaEncontrada);
        productoGuardado.setCategoria(categoriaEncontrada);

        // d. Definimos el comportamiento de los mocks
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marcaEncontrada));
        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoriaEncontrada));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);


        // --- ACT (Ejecutar el método a probar) ---
        ProductoDTO resultadoDTO = productoService.crearProducto(productoDTO_entrada);


        // --- ASSERT (Verificar el resultado) ---
        assertThat(resultadoDTO).isNotNull();
        assertThat(resultadoDTO.getId()).isEqualTo(99L);
        assertThat(resultadoDTO.getSku()).isEqualTo("BUZO-ROJO-S");
        assertThat(resultadoDTO.getMarcaNombre()).isEqualTo("Marca Famosa");
        assertThat(resultadoDTO.getCategoriaNombre()).isEqualTo("Buzos");

        // Verificamos que los métodos de los repositorios fueron llamados
        verify(marcaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).findById(10L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
}