package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}