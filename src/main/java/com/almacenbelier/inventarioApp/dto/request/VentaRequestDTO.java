package com.almacenbelier.inventarioApp.dto.request;

import com.almacenbelier.inventarioApp.dto.VentaItemDTO;
import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private List<VentaItemDTO> items;
}