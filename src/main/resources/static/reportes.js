document.addEventListener('DOMContentLoaded', () => {
    const ventasContainer = document.getElementById('ventas-container');
    const bajoStockContainer = document.getElementById('bajo-stock-container');
    const fechaDesdeInput = document.getElementById('fechaDesde');
    const fechaHastaInput = document.getElementById('fechaHasta');
    const filtrarBtn = document.getElementById('filtrarBtn');
    const totalVendidoSpan = document.getElementById('totalVendido');

    // Inicializar fechas (ej: último mes)
    const hoy = new Date();
    const haceUnMes = new Date(hoy);
    haceUnMes.setMonth(haceUnMes.getMonth() - 1);
    fechaDesdeInput.valueAsDate = haceUnMes;
    fechaHastaInput.valueAsDate = hoy;


    const fetchData = async (url) => {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Error ${response.status} al cargar datos de ${url}`);
        }
        // Si no hay contenido (ej: 204 No Content), devolvemos array vacío
        if (response.status === 204) {
            return [];
        }
        return response.json();
    };

    const cargarUltimasVentas = async () => {
        // Leemos las fechas seleccionadas
        const fechaDesde = fechaDesdeInput.value;
        const fechaHasta = fechaHastaInput.value;

        // Validamos que ambas fechas estén presentes
        if (!fechaDesde || !fechaHasta) {
            ventasContainer.innerHTML = '<p class="error">Por favor, seleccione ambas fechas.</p>';
            totalVendidoSpan.textContent = '0.00';
            return;
        }

        // Formateamos las fechas para la API (YYYY-MM-DDTHH:mm:ss)
        const fechaDesdeISO = `${fechaDesde}T00:00:00`;
        const fechaHastaISO = `${fechaHasta}T23:59:59`; // Usamos el final del día

        // Construimos la URL con los parámetros
        const url = `/api/ventas?fechaDesde=${fechaDesdeISO}&fechaHasta=${fechaHastaISO}`;

        try {
            ventasContainer.innerHTML = 'Cargando...'; // Feedback
            const ventas = (await fetchData(url)).sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

            if (ventas.length === 0) {
                ventasContainer.innerHTML = '<p>No hay ventas registradas en este período.</p>';
                totalVendidoSpan.textContent = '0.00';
                return;
            }

            let html = '<ul class="lista-ventas">';
            let totalPeriodo = 0; // Variable para sumar el total

            ventas.forEach(venta => {
                totalPeriodo += venta.total; // Sumamos el total de cada venta

                const fechaFormateada = new Date(venta.fecha).toLocaleString('es-AR', { /* ... opciones de formato ... */ });

                html += `<li class="venta-item"><div class="venta-header"><strong>Venta #${venta.id}</strong><span>${fechaFormateada}</span><span>Total: $${venta.total.toFixed(2)}</span></div><ul class="venta-item-detalles">`;
                venta.items.forEach(item => {
                    html += `<li>${item.cantidad} x ${item.nombreProducto} (@ $${item.precioUnitario.toFixed(2)} c/u)</li>`;
                });
                html += `</ul></li>`;
            });
            html += '</ul>';
            ventasContainer.innerHTML = html;
            totalVendidoSpan.textContent = totalPeriodo.toFixed(2); // Mostramos el total calculado

        } catch (error) {
            console.error(error);
            ventasContainer.innerHTML = '<p class="error">No se pudieron cargar las ventas.</p>';
            totalVendidoSpan.textContent = 'Error';
        }
    };

    const cargarBajoStock = async () => {
        // Esta función queda igual que antes
        try {
            bajoStockContainer.innerHTML = 'Cargando...';
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

    // Event listener para el botón de filtrar
    filtrarBtn.addEventListener('click', cargarUltimasVentas);

    // Carga inicial al entrar a la página
    cargarUltimasVentas();
    cargarBajoStock();
});