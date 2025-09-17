package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
}