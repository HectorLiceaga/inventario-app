package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.model.Marca;
import com.almacenbelier.inventarioApp.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaRepository marcaRepository; // Para operaciones simples, podemos usar el repo directamente

    // Endpoint para OBTENER TODAS las marcas
    @GetMapping
    public ResponseEntity<List<Marca>> obtenerTodasLasMarcas() {
        List<Marca> marcas = marcaRepository.findAll();
        return ResponseEntity.ok(marcas);
    }

    // Endpoint para CREAR una nueva marca
    @PostMapping
    public ResponseEntity<Marca> crearMarca(@RequestBody Marca marca) {
        // En una app real, usarías DTOs y un Service para validaciones.
        // Para este MVP, esto es suficiente.
        Marca nuevaMarca = marcaRepository.save(marca);
        return new ResponseEntity<>(nuevaMarca, HttpStatus.CREATED);
    }
}
