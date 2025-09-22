package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySku(String sku); // Añade esta línea
}