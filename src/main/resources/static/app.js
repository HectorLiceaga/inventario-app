// --- 1. SELECCIÓN DE ELEMENTOS DEL DOM ---
const skuInput = document.getElementById('skuInput');
const startCameraBtn = document.getElementById('start-camera-btn');
const videoContainer = document.getElementById('video-container');
const resultContainer = document.getElementById('result-container');

let html5QrCode = null; // Variable para guardar la instancia del lector

// --- 2. FUNCIONES PRINCIPALES ---

/**
 * Llama a la API de Spring Boot para buscar un producto por su SKU.
 * @param {string} sku El SKU a buscar.
 */
async function buscarProductoPorSku(sku) {
    resultContainer.innerHTML = '<p>Buscando...</p>'; // Feedback para el usuario

    // NOTA: Debes crear este endpoint en tu backend si no lo tienes
    const API_URL = `/api/productos/sku/${sku}`;

    try {
        const response = await fetch(API_URL);

        if (response.ok) {
            const producto = await response.json();
            mostrarResultados(producto);
        } else if (response.status === 404) {
            mostrarError(`Producto con SKU "${sku}" no encontrado.`);
        } else {
            mostrarError('Error en la respuesta del servidor.');
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        mostrarError('No se pudo conectar con el servidor. ¿Está encendido?');
    }
}

/**
 * Muestra la información del producto en el contenedor de resultados.
 * @param {object} producto El objeto producto recibido de la API.
 */
function mostrarResultados(producto) {
    resultContainer.innerHTML = `
        <div class="producto">
            <p><strong>Nombre:</strong> ${producto.nombre}</p>
            <p><strong>SKU:</strong> ${producto.sku}</p>
            <p><strong>Precio:</strong> $${producto.precioVenta}</p>
            <p><strong>Stock:</strong> ${producto.stock} unidades</p>
        </div>
    `;
}

/**
 * Muestra un mensaje de error en el contenedor de resultados.
 * @param {string} mensaje El mensaje de error a mostrar.
 */
function mostrarError(mensaje) {
    resultContainer.innerHTML = `<p class="error">${mensaje}</p>`;
}

/**
 * Inicia el escaneo con la cámara del dispositivo.
 */
function iniciarEscaneoCamara() {
    // Si ya hay una instancia, no hagas nada
    if (html5QrCode && html5QrCode.isScanning) {
        return;
    }

    html5QrCode = new Html5Qrcode("video-container"); // Usamos el div del HTML

    const onScanSuccess = (decodedText, decodedResult) => {
        // Cuando encuentra un código, lo pone en el input y detiene la cámara
        console.log(`Código encontrado: ${decodedText}`);
        skuInput.value = decodedText;

        // Dispara la búsqueda automáticamente
        buscarProductoPorSku(decodedText);

        // Detiene el escáner
        html5QrCode.stop().then(() => {
            console.log("Escáner detenido.");
            videoContainer.style.display = 'none'; // Oculta el contenedor del video
        }).catch(err => console.error(err));
    };

    const config = { fps: 10, qrbox: 250 }; // Configuración del escáner
    videoContainer.style.display = 'block'; // Muestra el contenedor del video

    // Inicia la cámara trasera ("environment")
    html5QrCode.start({ facingMode: "environment" }, config, onScanSuccess)
        .catch(err => {
            console.error("Error al iniciar la cámara", err);
            mostrarError("No se pudo iniciar la cámara.");
        });
}


// --- 3. ASIGNACIÓN DE EVENTOS ---

// Evento para el botón de la cámara
startCameraBtn.addEventListener('click', iniciarEscaneoCamara);

// Evento para el input (útil para el lector bluetooth que simula un "Enter")
skuInput.addEventListener('keyup', (event) => {
    if (event.key === 'Enter' && skuInput.value) {
        buscarProductoPorSku(skuInput.value);
    }
});