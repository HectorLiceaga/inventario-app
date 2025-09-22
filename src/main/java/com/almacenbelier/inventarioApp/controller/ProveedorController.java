package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.model.Proveedor;
import com.almacenbelier.inventarioApp.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorRepository proveedorRepository; // Para operaciones simples, podemos usar el repo directamente

    // Endpoint para OBTENER TODAS las proveedores
    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerTodasLasMarcas() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return ResponseEntity.ok(proveedores);
    }

    // Endpoint para CREAR una nueva marca
    @PostMapping
    public ResponseEntity<Proveedor> crearMarca(@RequestBody Proveedor proveedor) {
        // En una app real, usarías DTOs y un Service para validaciones.
        // Para este MVP, esto es suficiente.
        Proveedor nuevoProveedor = proveedorRepository.save(proveedor);
        return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
    }
}