package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta,Long> {
    List<Venta> findByFechaBetweenOrderByFechaDesc(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
