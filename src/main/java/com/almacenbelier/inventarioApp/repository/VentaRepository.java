package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta,Long> {
}
