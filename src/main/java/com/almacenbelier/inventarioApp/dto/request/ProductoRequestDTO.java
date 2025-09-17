package com.almacenbelier.inventarioApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoRequestDTO {

    // No incluimos el ID, porque al crear un producto, aún no existe.

    @NotBlank(message = "El SKU no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El SKU debe tener entre 3 y 50 caracteres.")
    private String sku;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    @NotBlank(message = "El talle no puede estar vacío.")
    private String talle;

    @NotBlank(message = "El color no puede estar vacío.")
    private String color;

    @NotNull(message = "El precio de venta es obligatorio.")
    @Positive(message = "El precio de venta debe ser un número positivo.")
    private Double precioVenta;

    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser un número negativo.")
    private Integer stock;

    @NotNull(message = "El ID de la marca es obligatorio.")
    private Long marcaId;

    @NotNull(message = "El ID de la categoría es obligatorio.")
    private Long categoriaId;
}