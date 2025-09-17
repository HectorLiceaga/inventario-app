package com.almacenbelier.inventarioApp.dto.response;

import lombok.Data;

@Data
public class ProductoResponseDTO {

    // Aquí SÍ incluimos el ID, porque el producto ya existe en la base de datos.
    private Long id;

    private String sku;
    private String nombre;
    private String talle;
    private String color;
    private Double precioVenta;
    private int stock;

    // Para la respuesta, es muy útil incluir no solo los IDs de las relaciones,
    // sino también sus nombres. Esto le ahorra trabajo al frontend.
    private Long marcaId;
    private String marcaNombre;

    private Long categoriaId;
    private String categoriaNombre;
}