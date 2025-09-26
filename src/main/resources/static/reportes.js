document.addEventListener('DOMContentLoaded', () => {
    const ventasContainer = document.getElementById('ventas-container');
    const bajoStockContainer = document.getElementById('bajo-stock-container');

    const fetchData = async (url) => {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Error al cargar datos de ${url}`);
        }
        return response.json();
    };

    const cargarUltimasVentas = async () => {
        try {
            // Ordenamos las ventas de la más reciente a la más antigua
            const ventas = (await fetchData('/api/ventas')).sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

            if (ventas.length === 0) {
                ventasContainer.innerHTML = '<p>No hay ventas registradas.</p>';
                return;
            }

            let html = '<ul class="lista-ventas">';
            ventas.forEach(venta => {
                // Formateamos la fecha para que sea más legible
                const fechaFormateada = new Date(venta.fecha).toLocaleString('es-AR', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                });

                html += `
                    <li class="venta-item">
                        <div class="venta-header">
                            <strong>Venta #${venta.id}</strong>
                            <span>${fechaFormateada}</span>
                            <span>Total: $${venta.total.toFixed(2)}</span>
                        </div>
                        <ul class="venta-item-detalles">
                `;

                // --- AQUÍ ESTÁ LA MAGIA ---
                // Iteramos sobre los items de CADA venta
                venta.items.forEach(item => {
                    html += `
                        <li>
                            ${item.cantidad} x ${item.nombreProducto} 
                            (@ $${item.precioUnitario.toFixed(2)} c/u)
                        </li>
                    `;
                });

                html += `
                        </ul>
                    </li>
                `;
            });
            html += '</ul>';
            ventasContainer.innerHTML = html;
        } catch (error) {
            console.error(error);
            ventasContainer.innerHTML = '<p class="error">No se pudieron cargar las ventas.</p>';
        }
    };

    const cargarBajoStock = async () => {
        try {
            const productos = await fetchData('/api/productos/bajo-stock');
            if (productos.length === 0) {
                bajoStockContainer.innerHTML = '<p>No hay productos con bajo stock.</p>';
                return;
            }
            let html = '<ul class="lista-bajo-stock">';
            productos.forEach(producto => {
                html += `<li>${producto.nombre} (SKU: ${producto.sku}) - <strong>Stock: ${producto.stock}</strong></li>`;
            });
            html += '</ul>';
            bajoStockContainer.innerHTML = html;
        } catch (error) {
            console.error(error);
            bajoStockContainer.innerHTML = '<p class="error">No se pudo cargar el reporte de stock.</p>';
        }
    };

    cargarUltimasVentas();
    cargarBajoStock();
});