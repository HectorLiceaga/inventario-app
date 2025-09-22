package com.almacenbelier.inventarioApp.service;

import com.almacenbelier.inventarioApp.dto.request.VentaRequestDTO;
import com.almacenbelier.inventarioApp.model.Producto;
import com.almacenbelier.inventarioApp.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final ProductoRepository productoRepository;

    @Transactional // ¡MUY IMPORTANTE!
    public void realizarVenta(VentaRequestDTO ventaDTO) {
        ventaDTO.getItems().forEach(item -> {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        });
    }
}
