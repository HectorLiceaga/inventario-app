package com.almacenbelier.inventarioApp.controller;

import com.almacenbelier.inventarioApp.dto.request.VentaRequestDTO;
import com.almacenbelier.inventarioApp.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
