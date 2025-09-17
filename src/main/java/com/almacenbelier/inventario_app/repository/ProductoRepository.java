package com.almacenbelier.inventario_app.repository;

import com.almacenbelier.inventario_app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}