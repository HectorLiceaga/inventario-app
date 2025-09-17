package com.almacenbelier.inventarioApp.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;
    private String sku;
    private String nombre;
    private String talle;
    private String color;
    private Double precioVenta;
    private int stock;

    // Para las relaciones, solo necesitamos los IDs para crear/actualizar,
    // y los nombres para mostrar. Vamos a simplificarlo en un solo DTO.
    private Long marcaId;
    private String marcaNombre;

    private Long categoriaId;
    private String categoriaNombre;
}