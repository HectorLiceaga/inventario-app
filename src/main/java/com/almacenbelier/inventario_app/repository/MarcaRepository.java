package com.almacenbelier.inventario_app.repository;

import com.almacenbelier.inventario_app.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}