package com.almacenbelier.inventario_app.controller;

import com.almacenbelier.inventario_app.dto.ProductoDTO;
import com.almacenbelier.inventario_app.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos") // La URL base para todos los endpoints de productos
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Endpoint para CREAR un producto (POST)
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED); // Devuelve 201 Created
    }

    // Endpoint para OBTENER TODOS los productos (GET)
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK); // Devuelve 200 OK
    }

    // Endpoint para OBTENER UN producto por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    // Endpoint para ELIMINAR un producto (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content
    }

    // Faltaría el endpoint para ACTUALIZAR (PUT), pero con esto ya tienes un CRUD casi completo.
}

