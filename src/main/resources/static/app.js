document.addEventListener('DOMContentLoaded', () => {
    // --- STATE ---
    let carrito = [];
    let productoEncontrado = null;
    let html5QrCode = null;

    // --- DOM ELEMENTS ---
    const skuInput = document.getElementById('skuInput');
    const infoProducto = document.getElementById('info-producto');
    const cartItemsContainer = document.getElementById('cart-items');
    const cartTotalAmount = document.getElementById('cart-total-amount');
    const finalizarVentaBtn = document.getElementById('finalizar-venta-btn');
    const nuevoProductoContainer = document.getElementById('nuevo-producto-container');
    const formNuevoProducto = document.getElementById('form-nuevo-producto');
    const cancelarCargaBtn = document.getElementById('cancelar-carga-btn');
    const selects = {
        marca: document.getElementById('marca-select'),
        categoria: document.getElementById('categoria-select')
    };
    const startCameraBtn = document.getElementById('start-camera-btn');
    const cameraContainer = document.getElementById('camera-container');

    // --- LÓGICA DE LA CÁMARA ---
    const iniciarEscaneoCamara = () => {
        // ... (código de la cámara queda igual) ...
        if (html5QrCode && html5QrCode.isScanning) {
            html5QrCode.stop().then(() => {
                cameraContainer.classList.add('hidden');
            }).catch(err => console.error("Error al detener la cámara.", err));
            return;
        }
        html5QrCode = new Html5Qrcode("camera-container");
        cameraContainer.classList.remove('hidden');
        const onScanSuccess = (decodedText, decodedResult) => {
            skuInput.value = decodedText;
            html5QrCode.stop().then(() => {
                cameraContainer.classList.add('hidden');
            }).catch(err => console.error("Error al detener la cámara.", err));
            buscarProducto(); // Llama a la función buscarProducto actualizada
        };
        const config = { fps: 10, qrbox: { width: 250, height: 150 } };
        html5QrCode.start({ facingMode: "environment" }, config, onScanSuccess)
            .catch(err => {
                console.error("Error al iniciar la cámara", err);
                cameraContainer.classList.add('hidden');
                infoProducto.innerHTML = `<p class="error">No se pudo iniciar la cámara.</p>`;
            });
    };

    // --- LÓGICA PRINCIPAL (ACTUALIZADA) ---
    const buscarProducto = async () => {
        // Ahora lee SKU o Código Interno
        const identificador = skuInput.value.trim();
        if (!identificador) return;
        try {
            // Llama al nuevo endpoint unificado
            const producto = await fetchData(`/api/productos/identificador/${identificador}`);
            productoEncontrado = producto;
            mostrarProductoParaAgregar(producto);
        } catch (error) {
            // Muestra error y prepara formulario de carga
            infoProducto.innerHTML = `<p class="error">Producto con ID "${identificador}" no encontrado.</p>`;
            document.getElementById('sku-carga').value = identificador; // Rellena SKU
            document.getElementById('codigoInterno-carga').value = ''; // Limpia Cod Interno
            nuevoProductoContainer.classList.remove('hidden');
        } finally {
            skuInput.value = ''; // Limpia input principal
        }
    };

    // ACTUALIZADO: Muestra Codigo Interno si existe
    const mostrarProductoParaAgregar = (producto) => {
        let displayText = `${producto.nombre} ($${producto.precioVenta.toFixed(2)}) - Stock: ${producto.stock}`;
        // Si el producto tiene codigoInterno y no es vacío, lo muestra
        if (producto.codigoInterno) {
            displayText += ` (Cod: ${producto.codigoInterno})`;
        }
        infoProducto.innerHTML = `<div class="producto-encontrado"><span>${displayText}</span><button id="add-to-cart-btn" title="Agregar al Carrito">Agregar al Carrito</button></div>`;
        // Usamos el texto original del botón por ahora para evitar problemas de layout
    };

    // --- LÓGICA DEL CARRITO (sin cambios) ---
    const agregarAlCarrito = (producto) => {
        if (!producto) return;
        const itemExistente = carrito.find(item => item.id === producto.id);
        if (itemExistente) {
            if (itemExistente.cantidad < producto.stock) { itemExistente.cantidad++; }
            else { alert(`Stock máximo para ${producto.nombre} alcanzado.`); }
        } else {
            if (producto.stock > 0) { carrito.push({ ...producto, cantidad: 1 }); }
            else { alert(`${producto.nombre} no tiene stock.`); }
        }
        renderizarCarrito();
        productoEncontrado = null;
        infoProducto.innerHTML = '';
    };

    const actualizarCantidad = (productoId, nuevaCantidad) => {
        const item = carrito.find(item => item.id === productoId);
        if (!item) return;
        if (nuevaCantidad > 0 && nuevaCantidad <= item.stock) { item.cantidad = nuevaCantidad; }
        else if (nuevaCantidad === 0) { carrito = carrito.filter(i => i.id !== productoId); }
        renderizarCarrito();
    };

    const renderizarCarrito = () => {
        cartItemsContainer.innerHTML = '';
        if (carrito.length === 0) {
            cartItemsContainer.innerHTML = '<p class="empty-cart">El carrito está vacío.</p>';
            finalizarVentaBtn.disabled = true;
            cartTotalAmount.textContent = '0.00';
        } else {
            let total = 0;
            carrito.forEach(item => {
                total += item.precioVenta * item.cantidad;
                // Usamos la 'X' original por ahora
                cartItemsContainer.innerHTML += `<div class="cart-item"><div class="cart-item-info"><p class="item-name">${item.nombre}</p><p>$${item.precioVenta.toFixed(2)}</p></div><div class="cart-item-controls"><button class="qty-btn" data-id="${item.id}" data-action="decrease">-</button><span>${item.cantidad}</span><button class="qty-btn" data-id="${item.id}" data-action="increase">+</button><button class="remove-item-btn" data-id="${item.id}">X</button></div></div>`;
            });
            cartTotalAmount.textContent = total.toFixed(2);
            finalizarVentaBtn.disabled = false;
        }
    };

    // --- OTRAS FUNCIONES ---
    const fetchData = async (url, options = {}) => {
        const response = await fetch(url, options);
        if (!response.ok) { throw new Error(`HTTP error! status: ${response.status}`); }
        // Manejo especial para respuestas sin contenido (como un DELETE exitoso)
        if (response.status === 204) { return null; } // O puedes devolver un objeto vacío {} si prefieres
        return response.json();
    };
    const inicializarSelects = () => {
        cargarOpciones('/api/marcas', selects.marca, 'nombre');
        cargarOpciones('/api/categorias', selects.categoria, 'nombre');
        // Falta cargar proveedores si los usas
    };
    const ocultarFormulario = () => {
        nuevoProductoContainer.classList.add('hidden');
        formNuevoProducto.reset();
    };
    const cargarOpciones = async (url, selectElement, nombreCampo) => {
        try {
            const data = await fetchData(url);
            selectElement.innerHTML = '<option value="">Seleccione...</option>';
            data.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item[nombreCampo];
                selectElement.appendChild(option);
            });
        } catch (error) { console.error(`Error al cargar opciones para ${selectElement.id}`); }
    };
    const agregarNuevaEntidad = async (tipo) => {
        const nombre = prompt(`Ingrese el nombre de la nueva ${tipo}:`);
        if (!nombre) return;
        try {
            const nuevaEntidad = await fetchData(`/api/${tipo}s`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nombre }) });
            await cargarOpciones(`/api/${tipo}s`, selects[tipo], 'nombre');
            selects[tipo].value = nuevaEntidad.id; // Selecciona la nueva opción
        } catch (error) { alert(`Error al crear la nueva ${tipo}.`); }
    };

    // ACTUALIZADO: Lee el campo codigoInterno-carga
    const guardarNuevoProducto = async (event) => {
        event.preventDefault();
        const codigoInternoInput = document.getElementById('codigoInterno-carga').value.trim();
        const requestDTO = {
            sku: document.getElementById('sku-carga').value,
            // Si el campo está vacío, manda null; si no, manda el valor
            codigoInterno: codigoInternoInput === '' ? null : codigoInternoInput,
            nombre: document.getElementById('nombre-carga').value,
            talle: document.getElementById('talle-carga').value,
            color: document.getElementById('color-carga').value,
            precioVenta: parseFloat(document.getElementById('precio-carga').value),
            stock: parseInt(document.getElementById('stock-carga').value),
            marcaId: parseInt(selects.marca.value),
            categoriaId: parseInt(selects.categoria.value)
            // Falta proveedorId si lo usas
        };

        // Validación simple
        if (!requestDTO.sku || !requestDTO.nombre || !requestDTO.marcaId || !requestDTO.categoriaId) {
            alert('Por favor complete todos los campos obligatorios (SKU, Nombre, Marca, Categoría).');
            return;
        }

        try {
            await fetchData('/api/productos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestDTO)
            });
            alert('Producto guardado con éxito!');
            ocultarFormulario();
        } catch (error) {
            alert('Error al guardar el producto. Verifique si el SKU o Código Interno ya existen.');
            console.error("Error guardando producto:", error);
        }
    };

    const finalizarVenta = async () => {
        // ... (lógica de finalizar venta queda igual) ...
        const ventaDTO = { items: carrito.map(item => ({ productoId: item.id, cantidad: item.cantidad })) };
        try {
            await fetch('/api/ventas', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(ventaDTO) });
            alert('Venta realizada con éxito!');
            carrito = [];
            renderizarCarrito();
        } catch (error) { alert('Error al procesar la venta. Verifique el stock.'); }
    };

    // --- EVENT LISTENERS ---
    skuInput.addEventListener('change', buscarProducto); // Se activa al perder foco o presionar Enter
    startCameraBtn.addEventListener('click', iniciarEscaneoCamara);
    finalizarVentaBtn.addEventListener('click', finalizarVenta);
    formNuevoProducto.addEventListener('submit', guardarNuevoProducto);
    cancelarCargaBtn.addEventListener('click', ocultarFormulario);
    document.querySelectorAll('.add-btn').forEach(btn => {
        btn.addEventListener('click', () => agregarNuevaEntidad(btn.dataset.tipo));
    });
    // Listener para botones de agregar al carrito (se añade dinámicamente)
    infoProducto.addEventListener('click', (e) => {
        if (e.target && e.target.id === 'add-to-cart-btn') {
            agregarAlCarrito(productoEncontrado);
        }
    });
    // Listener para controles del carrito (+, -, X)
    cartItemsContainer.addEventListener('click', (e) => {
        const target = e.target;
        // Solo reacciona si el elemento clickeado tiene data-id
        if (!target.dataset.id) return;
        const productoId = parseInt(target.dataset.id);
        const item = carrito.find(item => item.id === productoId);
        if (!item) return; // Seguridad extra

        if (target.classList.contains('qty-btn')) {
            const action = target.dataset.action;
            const newQty = action === 'increase' ? item.cantidad + 1 : item.cantidad - 1;
            actualizarCantidad(productoId, newQty);
        } else if (target.classList.contains('remove-item-btn')) {
            actualizarCantidad(productoId, 0); // Poner cantidad a 0 lo elimina
        }
    });

    // --- INITIAL LOAD ---
    inicializarSelects();
});