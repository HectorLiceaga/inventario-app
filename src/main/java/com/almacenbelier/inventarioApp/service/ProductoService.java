package com.almacenbelier.inventarioApp.service; // Asegúrate que el package sea el correcto

import com.almacenbelier.inventarioApp.dto.request.ProductoRequestDTO;   // Verifica tus imports de DTOs
import com.almacenbelier.inventarioApp.dto.response.ProductoResponseDTO; // Verifica tus imports de DTOs
import com.almacenbelier.inventarioApp.model.Categoria;
import com.almacenbelier.inventarioApp.model.Marca;
import com.almacenbelier.inventarioApp.model.Producto;
import com.almacenbelier.inventarioApp.repository.CategoriaRepository;
import com.almacenbelier.inventarioApp.repository.MarcaRepository;
import com.almacenbelier.inventarioApp.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import necesario si usas @Transactional

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    // Falta añadir ProveedorRepository si lo estás usando

    // --- MÉTODOS PÚBLICOS ---

    // NUEVO MÉTODO DE BÚSQUEDA UNIFICADO (reemplaza obtenerProductoPorSku)
    @Transactional(readOnly = true) // Buena práctica para métodos de solo lectura
    public ProductoResponseDTO obtenerProductoPorIdentificador(String identificador) {
        Producto producto = productoRepository.findBySkuOrCodigoInterno(identificador, identificador)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con SKU o Código Interno: " + identificador));
        return convertirA_ResponseDTO(producto);
    }

    // MÉTODO crearProducto (sin cambios funcionales, usa el helper actualizado)
    @Transactional // Buena práctica para métodos que modifican datos
    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        Producto producto = convertirA_Entidad(requestDTO, null); // Pasamos null para el ID al crear
        // Aquí podrías añadir validaciones, ej: verificar si SKU o codigoInterno ya existen
        Producto productoGuardado = productoRepository.save(producto);
        return convertirA_ResponseDTO(productoGuardado);
    }

    // MÉTODO actualizarProducto (usa el helper actualizado)
    @Transactional // Buena práctica para métodos que modifican datos
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        Producto productoExistente = buscarProductoPorId(id); // Verifica que exista primero
        Producto productoActualizado = convertirA_Entidad(requestDTO, productoExistente); // Reutilizamos el helper
        productoActualizado = productoRepository.save(productoActualizado);
        return convertirA_ResponseDTO(productoActualizado);
    }

    // MÉTODO obtenerTodosLosProductos (sin cambios)
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
    }

    // MÉTODO obtenerProductoPorId (sin cambios)
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        Producto producto = buscarProductoPorId(id);
        return convertirA_ResponseDTO(producto);
    }

    // MÉTODO eliminarProducto (sin cambios)
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    // MÉTODO obtenerProductosConBajoStock (sin cambios)
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerProductosConBajoStock() {
        int umbralBajoStock = 5;
        return productoRepository.findByStockLessThanEqual(umbralBajoStock)
                .stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
    }


    // --- MÉTODOS PRIVADOS DE AYUDA (Helpers) ---

    // MÉTODO buscarProductoPorId (sin cambios)
    private Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
    }

    // MÉTODO convertirA_ResponseDTO (ACTUALIZADO para incluir codigoInterno)
    private ProductoResponseDTO convertirA_ResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setSku(producto.getSku());
        dto.setCodigoInterno(producto.getCodigoInterno()); // <-- CAMBIO AQUÍ
        dto.setNombre(producto.getNombre());
        dto.setTalle(producto.getTalle());
        dto.setColor(producto.getColor());
        dto.setPrecioVenta(producto.getPrecioVenta());
        dto.setStock(producto.getStock());
        // Asumiendo que Marca y Categoria no son null
        if (producto.getMarca() != null) {
            dto.setMarcaId(producto.getMarca().getId());
            dto.setMarcaNombre(producto.getMarca().getNombre());
        }
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        // Falta añadir mapeo de Proveedor si lo usas
        return dto;
    }

    // MÉTODO convertirA_Entidad (ACTUALIZADO para manejar creación/actualización y codigoInterno)
    private Producto convertirA_Entidad(ProductoRequestDTO requestDTO, Producto productoExistente) {
        // Si no se provee una entidad existente, creamos una nueva; si no, usamos la existente.
        Producto producto = (productoExistente != null) ? productoExistente : new Producto();

        // Buscamos las entidades relacionadas (Marca y Categoria)
        Marca marca = marcaRepository.findById(requestDTO.getMarcaId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + requestDTO.getMarcaId()));
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + requestDTO.getCategoriaId()));
        // Falta buscar Proveedor si lo usas

        // Mapeamos los datos del DTO a la Entidad
        producto.setSku(requestDTO.getSku());
        producto.setCodigoInterno(requestDTO.getCodigoInterno()); // <-- CAMBIO AQUÍ
        producto.setNombre(requestDTO.getNombre());
        producto.setTalle(requestDTO.getTalle());
        producto.setColor(requestDTO.getColor());
        producto.setPrecioVenta(requestDTO.getPrecioVenta());
        producto.setStock(requestDTO.getStock());
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        // Falta setProveedor si lo usas

        return producto;
    }

    // Este métordo ya no es necesario, usamos obtenerProductoPorIdentificador en su lugar.
    // Lo comento por si lo necesitas por alguna otra razón.
    /*
    public ProductoResponseDTO obtenerProductoPorSku(String sku) {
        Producto producto = productoRepository.findBySku(sku) // Asume que findBySku existe en el repo
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con SKU: " + sku));
        return convertirA_ResponseDTO(producto);
    }
    */
}