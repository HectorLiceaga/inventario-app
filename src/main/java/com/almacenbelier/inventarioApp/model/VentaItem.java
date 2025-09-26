package com.almacenbelier.inventarioApp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VentaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private Double precioUnitario; // Guardamos el precio al momento de la venta

    // Muchos VentaItems pertenecen a una Venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
}