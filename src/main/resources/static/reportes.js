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
            const ventas = await fetchData('/api/ventas');
            if (ventas.length === 0) {
                ventasContainer.innerHTML = '<p>No hay ventas registradas.</p>';
                return;
            }
            // Aquí puedes construir una tabla o lista con los datos de las ventas
            let html = '<ul>';
            ventas.forEach(venta => {
                html += `<li>Venta #${venta.id} - Total: $${venta.total.toFixed(2)}</li>`;
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
            let html = '<ul>';
            productos.forEach(producto => {
                html += `<li>${producto.nombre} (SKU: ${producto.sku}) - Stock: ${producto.stock}</li>`;
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