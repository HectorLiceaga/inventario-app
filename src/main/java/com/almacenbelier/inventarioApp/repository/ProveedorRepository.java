package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor,Long> {
}
