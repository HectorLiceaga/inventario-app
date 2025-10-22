package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.dto.request.ProductoRequestDTO;
import com.almacenbelier.inventarioApp.dto.response.ProductoResponseDTO;
import com.almacenbelier.inventarioApp.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        // La anotación @Valid le dice a Spring que aplique las reglas de validación del DTO.
        // Si alguna regla falla, Spring devolverá automáticamente un error 400 Bad Request.
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos); // .ok() es un atajo para new ResponseEntity<>(..., HttpStatus.OK)
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO productoActualizado = productoService.actualizarProducto(id, requestDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build(); // .noContent().build() es un atajo para HttpStatus.NO_CONTENT
    }

    @GetMapping("/identificador/{identificador}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorIdentificador(@PathVariable String identificador) {
        try {
            ProductoResponseDTO producto = productoService.obtenerProductoPorIdentificador(identificador);
            return ResponseEntity.ok(producto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no se encuentra
        }
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosConBajoStock() {
        List<ProductoResponseDTO> productos = productoService.obtenerProductosConBajoStock();
        return ResponseEntity.ok(productos);
    }
}

