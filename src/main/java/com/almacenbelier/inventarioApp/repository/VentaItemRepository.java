package com.almacenbelier.inventarioApp.repository;

import com.almacenbelier.inventarioApp.model.VentaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaItemRepository extends JpaRepository<VentaItem,Long> {
}
