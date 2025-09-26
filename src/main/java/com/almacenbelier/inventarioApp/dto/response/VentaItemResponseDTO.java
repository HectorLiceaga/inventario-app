package com.almacenbelier.inventarioApp.dto.response;

import lombok.Data;

@Data
public class VentaItemResponseDTO {
    private String nombreProducto;
    private int cantidad;
    private Double precioUnitario;
}
