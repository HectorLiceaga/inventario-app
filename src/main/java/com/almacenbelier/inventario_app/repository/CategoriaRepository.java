package com.almacenbelier.inventario_app.repository;

import com.almacenbelier.inventario_app.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
