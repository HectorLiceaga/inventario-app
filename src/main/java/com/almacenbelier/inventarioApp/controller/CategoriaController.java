package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.model.Categoria;
import com.almacenbelier.inventarioApp.model.Marca;
import com.almacenbelier.inventarioApp.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository categoriaRepository; // Para operaciones simples, podemos usar el repo directamente

    // Endpoint para OBTENER TODAS las Categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return ResponseEntity.ok(categorias);
    }

    // Endpoint para CREAR una nueva categoria
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria cat) {
        // En una app real, usarías DTOs y un Service para validaciones.
        // Para este MVP, esto es suficiente.
        Categoria nuevaCat = categoriaRepository.save(cat);
        return new ResponseEntity<>(nuevaCat, HttpStatus.CREATED);
    }
}
