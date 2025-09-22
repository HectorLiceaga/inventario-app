package com.almacenbelier.inventarioApp.service;

import com.almacenbelier.inventarioApp.dto.request.ProductoRequestDTO;
import com.almacenbelier.inventarioApp.dto.response.ProductoResponseDTO;
import com.almacenbelier.inventarioApp.model.Categoria;
import com.almacenbelier.inventarioApp.model.Marca;
import com.almacenbelier.inventarioApp.model.Producto;
import com.almacenbelier.inventarioApp.repository.CategoriaRepository;
import com.almacenbelier.inventarioApp.repository.MarcaRepository;
import com.almacenbelier.inventarioApp.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Usamos Lombok para la inyección por constructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    // --- MÉTODOS PÚBLICOS ---

    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        // Convertimos el DTO de entrada a una entidad
        Producto producto = convertirA_Entidad(requestDTO);

        // Guardamos la nueva entidad en la base de datos
        Producto productoGuardado = productoRepository.save(producto);

        // Convertimos la entidad guardada a un DTO de respuesta y la devolvemos
        return convertirA_ResponseDTO(productoGuardado);
    }

    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirA_ResponseDTO) // Usamos el nuevo métordo de conversión
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        Producto producto = buscarProductoPorId(id);
        return convertirA_ResponseDTO(producto);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        // Primero, nos aseguramos de que el producto exista
        Producto productoExistente = buscarProductoPorId(id);

        // Actualizamos los campos de la entidad con los datos del DTO
        productoExistente.setSku(requestDTO.getSku());
        productoExistente.setNombre(requestDTO.getNombre());
        productoExistente.setTalle(requestDTO.getTalle());
        productoExistente.setColor(requestDTO.getColor());
        productoExistente.setPrecioVenta(requestDTO.getPrecioVenta());
        productoExistente.setStock(requestDTO.getStock());

        // Buscamos y actualizamos las relaciones si cambiaron
        Marca marca = marcaRepository.findById(requestDTO.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        productoExistente.setMarca(marca);
        productoExistente.setCategoria(categoria);

        Producto productoActualizado = productoRepository.save(productoExistente);
        return convertirA_ResponseDTO(productoActualizado);
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    // --- MÉTODOS PRIVADOS DE AYUDA (Helpers) ---

    private Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
    }

    private ProductoResponseDTO convertirA_ResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
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

    private Producto convertirA_Entidad(ProductoRequestDTO requestDTO) {
        Producto producto = new Producto();

        // Buscamos las entidades relacionadas (Marca y Categoria)
        Marca marca = marcaRepository.findById(requestDTO.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + requestDTO.getMarcaId()));
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));

        // Mapeamos los datos del DTO a la Entidad
        producto.setSku(requestDTO.getSku());
        producto.setNombre(requestDTO.getNombre());
        producto.setTalle(requestDTO.getTalle());
        producto.setColor(requestDTO.getColor());
        producto.setPrecioVenta(requestDTO.getPrecioVenta());
        producto.setStock(requestDTO.getStock());
        producto.setMarca(marca);
        producto.setCategoria(categoria);

        return producto;
    }

    public ProductoResponseDTO obtenerProductoPorSku(String sku) {
        Producto producto = productoRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con SKU: " + sku));
        return convertirA_ResponseDTO(producto);
    }
}