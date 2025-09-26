package com.almacenbelier.inventarioApp.service;

import com.almacenbelier.inventarioApp.dto.VentaItemDTO;
import com.almacenbelier.inventarioApp.dto.request.VentaRequestDTO;
import com.almacenbelier.inventarioApp.dto.response.VentaItemResponseDTO;
import com.almacenbelier.inventarioApp.dto.response.VentaResponseDTO;
import com.almacenbelier.inventarioApp.model.Producto;
import com.almacenbelier.inventarioApp.model.Venta;
import com.almacenbelier.inventarioApp.model.VentaItem;
import com.almacenbelier.inventarioApp.repository.ProductoRepository;
import com.almacenbelier.inventarioApp.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository; // Añadir el nuevo repositorio

    @Transactional
    public void realizarVenta(VentaRequestDTO ventaDTO) {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setFecha(LocalDateTime.now());

        double totalVenta = 0.0;

        for (VentaItemDTO itemDTO : ventaDTO.getItems()) {
            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            if (producto.getStock() < itemDTO.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
            }

            // Descontar stock (como antes)
            producto.setStock(producto.getStock() - itemDTO.getCantidad());
            productoRepository.save(producto);

            // Crear el VentaItem para el historial
            VentaItem ventaItem = new VentaItem();
            ventaItem.setProducto(producto);
            ventaItem.setCantidad(itemDTO.getCantidad());
            ventaItem.setPrecioUnitario(producto.getPrecioVenta());
            ventaItem.setVenta(nuevaVenta);

            nuevaVenta.getItems().add(ventaItem);
            totalVenta += ventaItem.getPrecioUnitario() * ventaItem.getCantidad();
        }

        nuevaVenta.setTotal(totalVenta);
        ventaRepository.save(nuevaVenta); // Guardar la venta y sus items en la BD
    }
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        return ventaRepository.findAll() // Asume que ordenará por más reciente
                .stream()
                .map(this::convertirVentaA_DTO)
                .collect(Collectors.toList());
    }

    private VentaResponseDTO convertirVentaA_DTO(Venta venta) {
        VentaResponseDTO ventaDTO = new VentaResponseDTO();
        ventaDTO.setId(venta.getId());
        ventaDTO.setFecha(venta.getFecha());
        ventaDTO.setTotal(venta.getTotal());

        List<VentaItemResponseDTO> itemsDTO = venta.getItems().stream().map(item -> {
            VentaItemResponseDTO itemDTO = new VentaItemResponseDTO();
            itemDTO.setNombreProducto(item.getProducto().getNombre());
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setPrecioUnitario(item.getPrecioUnitario());
            return itemDTO;
        }).collect(Collectors.toList());

        ventaDTO.setItems(itemsDTO);
        return ventaDTO;
    }
}
