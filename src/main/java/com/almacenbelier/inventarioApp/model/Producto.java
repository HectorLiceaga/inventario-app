package com.almacenbelier.inventarioApp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String talle;

    @Column(nullable = false)
    private String color;

    private Double precioCompra;

    @Column(nullable = false)
    private Double precioVenta;

    @Column(nullable = false)
    private int stock;

    // --- Relaciones ---
    // Muchos productos pueden tener una marca
    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    // Muchos productos pueden pertenecer a una categoría
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}