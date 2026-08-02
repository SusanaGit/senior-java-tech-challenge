# 🧪 Prueba Técnica – Sistema de Productos con Precios Históricos

## 1. Instrucciones para compilar y ejecutar el proyecto.

## 2. Justificación de decisiones técnicas.

### Creación del contract.yml

### Arquitectura Hexagonal

## 3. Indicaciones si agregaste mejoras, asumiste supuestos o cambiaste los endpoints.

### Modificación de los endpoints

He modificado el endpoint GET /products/{id}/*prices*?date=2024-04-15 cambiando el prices por price, ya que lo que quiero obtener es el precio del producto para la fecha específica y considero que se entiende mejor en singular. La otra opción se
interpretaría como aplicar el filtro de la fecha en la colección de precios del producto, pero me parece menos entendible.

## 4. Cómo ejecutar la prueba de rendimiento (si aplicaste ese desafío).









Tu objetivo es diseñar e implementar una API que permita gestionar productos y sus precios históricos. Cada producto puede tener múltiples precios a lo largo del tiempo, pero solo un precio puede estar vigente para una misma fecha.

---

## 🎯 Objetivo

Queremos que demuestres tus conocimientos técnicos, tu criterio para tomar decisiones de diseño, y tu capacidad para resolver un problema realista de backend.

Puedes usar el **framework que prefieras**, la **arquitectura que consideres apropiada** y la **base de datos que mejor se adapte a tu solución**. Algunas opciones válidas incluyen Spring Boot, Quarkus, Java puro, PostgreSQL, MongoDB, MySQL, H2, etc.

La implementación puede realizarse en **Java o Kotlin**.

⚠️ **Uno de los requisitos más importantes de esta prueba es que tu solución tenga el mejor rendimiento posible**, tanto en tiempo de respuesta como en uso eficiente de recursos.

---

## 📘 Requisitos funcionales

### Endpoints obligatorios

Debes implementar los siguientes endpoints:

1. **Crear un producto**
    - `POST /products`
    - Body:
      ```json
      {
        "name": "Zapatillas deportivas",
        "description": "Modelo 2025 edición limitada"
      }
      ```

2. **Agregar un precio a un producto**
    - `POST /products/{id}/prices`
    - Body:
      ```json
      {
        "value": 99.99,
        "initDate": "2024-01-01",
        "endDate": "2024-06-30"
      }
      ```
    - Reglas:
        - No debe haber solapamiento de fechas con otros precios del mismo producto.
        - `endDate` puede ser `null`.
        - Validar que `initDate` < `endDate` si ambas existen.

3. **Obtener el precio vigente de un producto en una fecha**
    - `GET /products/{id}/prices?date=2024-04-15`
    - Body:
      ```json
      {
        "value": 99.99
      }
      ```

4. **Obtener el historial completo de precios de un producto**
    - `GET /products/{id}/prices`
    - Body:
      ```json
      {
        "name": "Zapatillas deportivas",
        "description": "Modelo 2025 edición limitada",
        "prices": [
          {
            "value": 99.99,
            "initDate": "2024-01-01",
            "endDate": "2024-06-30"
          },
          {
            "value": 199.99,
            "initDate": "2025-01-01",
            "endDate": "2025-06-30"
          },
        ]
      }
      ```

📌 **Nota**:  
Los endpoints anteriores se utilizarán en las pruebas automáticas.  
Sin embargo, **si consideras que alguno puede mejorarse para alinearse mejor con la semántica REST**, puedes hacerlo libremente, justificándolo en el README de tu proyecto.

---

## ✅ Criterios de evaluación

- Modelado correcto de entidades y relaciones.
- Validación robusta de reglas de negocio.
- Diseño RESTful claro y consistente.
- Organización del código y buenas prácticas.
- Elección justificada del stack técnico.
- **Rendimiento**: arranque rápido, respuestas ágiles, bajo uso de recursos.
- Tests automatizados (unitarios o de integración).
- Claridad en la documentación y facilidad de ejecución.

---

## 🚀 Desafíos opcionales (bonus)

### 1. Prueba de rendimiento automatizada

Puedes incluir una prueba automática de performance para validar el comportamiento de tu API bajo carga.

#### ¿Qué debes entregar?

- Un archivo `docker-compose.yml` que:
    - Levante tu aplicación.
    - Ejecute un script o herramienta (por ejemplo, Gatling, k6, Artillery, JMeter, etc.) con múltiples peticiones concurrentes.

#### ¿Qué se evaluará?

- Tiempo de arranque de la aplicación.
- Velocidad de ejecución de los endpoints.
- Peticiones exitosas por segundo.
- Uso de recursos bajo carga.

#### Restricciones importantes:

- **No se podrán modificar los valores de CPU ni memoria del contenedor de la aplicación ni del script de rendimiento**.
- **Puedes añadir nuevos contenedores auxiliares**, siempre que **cada uno tenga un máximo de 1 GB de memoria y 500 Mi de CPU**.

Esto te permite aplicar estrategias como separación de servicios, caché, balanceo, precálculo, etc., **pero dentro de restricciones razonables de infraestructura**.

---

### 2. Otros desafíos opcionales

- Soporte para múltiples monedas por precio.
- Endpoint para actualizar o eliminar precios.
- Autenticación básica o con token.
- Documentación con Swagger/OpenAPI.
- Scripts para poblar datos de prueba automáticamente.
- Soporte para paginación, ordenamiento o filtrado en el historial de precios.
