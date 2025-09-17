package com.almacenbelier.inventario_app.service;

import com.almacenbelier.inventario_app.dto.ProductoDTO;
import com.almacenbelier.inventario_app.model.Categoria;
import com.almacenbelier.inventario_app.model.Marca;
import com.almacenbelier.inventario_app.model.Producto;
import com.almacenbelier.inventario_app.repository.CategoriaRepository;
import com.almacenbelier.inventario_app.repository.MarcaRepository;
import com.almacenbelier.inventario_app.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    // Método para crear un nuevo producto
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();

        // Buscamos las entidades relacionadas (Marca y Categoria)
        Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        // Mapeamos los datos del DTO a la Entidad
        producto.setSku(productoDTO.getSku());
        producto.setNombre(productoDTO.getNombre());
        producto.setTalle(productoDTO.getTalle());
        producto.setColor(productoDTO.getColor());
        producto.setPrecioVenta(productoDTO.getPrecioVenta());
        producto.setStock(productoDTO.getStock());
        producto.setMarca(marca);
        producto.setCategoria(categoria);

        // Guardamos el nuevo producto en la base de datos
        Producto productoGuardado = productoRepository.save(producto);

        // Convertimos la entidad guardada de nuevo a un DTO para devolverla
        return convertirA_DTO(productoGuardado);
    }

    // Método para obtener todos los productos
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirA_DTO) // Convertimos cada producto a DTO
                .collect(Collectors.toList());
    }

    // Método para obtener un producto por su ID
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return convertirA_DTO(producto);
    }

    // Método para eliminar un producto
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    // --- Métodos de ayuda (Helpers) ---

    // Este método privado convierte una Entidad Producto a un ProductoDTO
    private ProductoDTO convertirA_DTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setSku(producto.getSku());
        dto.setNombre(producto.getNombre());
        dto.setTalle(producto.getTalle());
        dto.setColor(producto.getColor());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setStock(producto.getStock());
        dto.setMarcaId(producto.getMarca().getId());
        dto.setMarcaNombre(producto.getMarca().getNombre());
        dto.setCategoriaId(producto.getCategoria().getId());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        return dto;
    }
}