package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.dto.request.VentaRequestDTO;
import com.almacenbelier.inventarioApp.dto.response.VentaResponseDTO;
import com.almacenbelier.inventarioApp.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<Void> procesarVenta(@RequestBody VentaRequestDTO ventaDTO) {
        try {
            ventaService.realizarVenta(ventaDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            // Esto sucede si no hay stock suficiente
            return ResponseEntity.status(409).build(); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> obtenerVentas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta) {

        // Si no vienen fechas, podemos poner valores por defecto, ej: último mes
        if (fechaDesde == null) {
            fechaDesde = LocalDateTime.now().minusMonths(1).withHour(0).withMinute(0).withSecond(0); // Inicio del mes pasado
        }
        if (fechaHasta == null) {
            fechaHasta = LocalDateTime.now(); // Hasta ahora
        }

        List<VentaResponseDTO> ventas = ventaService.obtenerVentas(fechaDesde, fechaHasta);
        return ResponseEntity.ok(ventas);
    }
}
