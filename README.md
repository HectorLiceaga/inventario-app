Informe de Proyecto: Inventario App (Almacén Belier)
1. Descripción General
   Inventario App es un sistema integral de punto de venta (POS) y gestión de existencias. Su objetivo principal es permitir la carga rápida de artículos mediante el escaneo de códigos de barras, el control de stock en tiempo real y la generación de reportes detallados de ventas y reposición.

2. Tecnologías Utilizadas

Backend: Java 17, Spring Boot 3, Spring Data JPA.

Base de Datos: PostgreSQL ejecutado en contenedores Docker.

Frontend: HTML5, CSS3, JavaScript (Vanilla JS), librería html5-qrcode.


DevOps: Docker, Docker Compose, Maven.

3. Relevamiento de la Arquitectura Backend
   Basado en el análisis del diagrama de clases (graphml), la estructura del backend se divide en las siguientes capas funcionales:

Modelo de Datos (Entidades)
Producto: Núcleo del inventario. Incluye campos para SKU (código de barras), código interno, nombre, talle, color, precio de venta, precio de compra y stock. Posee relaciones con Marca, Categoría y Proveedor.




Venta y VentaItem: Registran las transacciones. Una Venta contiene una lista de VentaItem, los cuales vinculan la cantidad vendida y el precio unitario con un Producto específico.



Marca, Categoria y Proveedor: Entidades de soporte para clasificar y organizar el catálogo de ropa.



Capa de Acceso a Datos (Repositories)
Se utilizan interfaces que extienden de JpaRepository para la persistencia.





ProductoRepository: Implementa búsquedas personalizadas como findBySkuOrCodigoInterno para la identificación dual y findByStockLessThanEqual para alertas de reposición.


VentaRepository: Permite el filtrado de transacciones por rango de fechas mediante el método findByFechaBetweenOrderByFechaDesc.

Capa de Servicio y Controladores

Servicios: ProductoService y VentaService encapsulan la lógica de negocio, incluyendo la conversión de entidades a DTOs (Data Transfer Objects) para una comunicación segura con el frontend.



Controladores REST: Exponen los endpoints para las operaciones CRUD y el procesamiento de ventas.




4. Hitos y Funcionalidades Implementadas
   Punto de Venta (POS) Móvil: Interfaz optimizada para celulares que utiliza la cámara para escanear SKUs y gestionar un carrito de compras dinámico.

Identificación Dual: Capacidad de buscar y cargar productos tanto por código de barras (SKU) como por código interno propio del negocio.

Procesamiento de Ventas: Lógica transaccional que descuenta el stock automáticamente al finalizar una compra y guarda el historial completo.

Módulo de Reportes: Visualización de ventas filtradas por fecha con cálculo automático de totales y alertas de bajo stock para productos con menos de 5 unidades.

Carga Rápida de Entidades: Posibilidad de crear marcas y categorías "al vuelo" mediante ventanas emergentes (prompt) sin salir del flujo de carga de productos.

Infraestructura Dockerizada: Despliegue simplificado mediante contenedores, resolviendo problemas críticos de codificación (UTF-8) y compatibilidad de construcción (buildx).

5. Próximos Pasos (Roadmap)
   Importación Masiva: Implementación de un módulo para cargar cientos de productos simultáneamente a través de archivos .csv.

Gestión de Precios: Incorporación de campos de precio de compra para calcular automáticamente la rentabilidad por prenda y por período.

Seguridad: Implementación de un sistema de login para proteger los datos de administración y reportes.