package com.almacenbelier.inventarioApp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private List<VentaItemResponseDTO> items;
}
