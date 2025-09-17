import com.almacenbelier.inventario_app.model.Producto;
import com.almacenbelier.inventario_app.repository.ProductoRepository;
import com.almacenbelier.inventario_app.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock // 1. Creamos un simulacro del Repositorio. No usaremos la BD real.
    private ProductoRepository productoRepository;

    @InjectMocks // 2. Creamos una instancia del Service e inyectamos el mock de arriba.
    private ProductoService productoService;

    @Test
    void deberiaGuardarUnProductoNuevo() {
        // Arrange: Preparar el escenario
        Producto productoAGuardar = new Producto();
        productoAGuardar.setSku("BUZO-ROJO-S");
        productoAGuardar.setNombre("Buzo Rojo Talle S");

        // Definimos que cuando se llame a repository.save(), nos devuelva el mismo producto
        when(productoRepository.save(any(Producto.class))).thenReturn(productoAGuardar);

        // Act: Ejecutar el método que estamos probando (aún no existe)
        Producto productoGuardado = productoService.guardarProducto(productoAGuardar);

        // Assert: Verificar el resultado
        assertThat(productoGuardado).isNotNull();
        assertThat(productoGuardado.getSku()).isEqualTo("BUZO-ROJO-S");

        // Verificamos que el método save() del repositorio fue llamado exactamente 1 vez.
        verify(productoRepository, times(1)).save(productoAGuardar);
    }
}