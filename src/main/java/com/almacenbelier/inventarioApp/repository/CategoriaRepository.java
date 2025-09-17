package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
