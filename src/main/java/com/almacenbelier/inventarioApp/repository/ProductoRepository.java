package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockLessThanEqual(int stockMinimo);

    Optional<Producto> findBySkuOrCodigoInterno(String sku, String codigoInterno);
}