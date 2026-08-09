# 🧪 Prueba Técnica – Sistema de Productos con Precios Históricos

## 1. Instrucciones para compilar y ejecutar el proyecto.

Situarse en el directorio donde se encuentra el docker-compose.yml y ejecutar docker compose up.

## 2. Justificación de decisiones técnicas.

### SpringBoot JPA

He creado las dos entidades para las dos tablas que necesito en esta implementación.

También añadidos los repositorios que tienen las queries para obtener la información que necesito de la base de datos.

### Flyway para crear y actualizar el esquema de la db

Flyway se encargará de crear y versionar el esquema de la base de datos. Los cambios en la base de datos se realizarán
ordenadamente y todos los componentes del equipo tendrán la versión de datos actualizada.

### Creación del contract.yml

Para generar automáticamente la interfaz que implementará el controller (definir los endpoints).

### Arquitectura Hexagonal

Imprescindible para no tener que modificar la lógica de negocio si surgen cambios en la parte de infraestructura.

### Tests MockK

Me ha parecido lo más óptimo ya que estoy codificando con Kotlin.

## 3. Indicaciones si agregaste mejoras, asumiste supuestos o cambiaste los endpoints.

### Modificación de los endpoints

He modificado el endpoint GET /products/{id}/ *prices*?date=2024-04-15 cambiando el prices por price, ya que lo que
quiero obtener es el precio del producto para la fecha específica y considero que se entiende mejor en singular. La otra
opción se interpretaría como aplicar el filtro de la fecha en la colección de precios del producto, pero me parece menos
entendible.

Al id le he llamado productId para que se interprete su significado de manera más sencilla.

## 4. Mejora del rendimiento

### Tiempo de arranque de la aplicación sin ninguna optimización (benchmark deshabilitado)

TIEMPO DE ARRANQUE SIN OPTIMIZACIONES: Root WebApplicationContext: initialization completed in 12095 ms

### Tiempo de arranque de la aplicación con mejoras (benchmark deshabilitado)

- Estoy usando Flyway, para que se encargue de mantener estable y actualizado el esquema de la base de datos. Así,
  Hibernate no se encargará de analizar el esquema, se reducirá el tiempo. Modifico SPRING_JPA_HIBERNATE_DDL_AUTO:
  update a validate en el docker-compose.yml, y en el application.yml.

```yml
  SPRING_JPA_HIBERNATE_DDL_AUTO: validate
```

```yml
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: false
```

- En el application.yml, hago que show-sql y properties.hibernate.format_sql sea false, ya que no lo necesito.

- Hago que la aplicación se compile de manera nativa, para reducir tareas realizadas durante el arranque al compilar la
  aplicación con JVM. La compilación AOT que se produce con graalvm transforma previamente la aplicación en código
  máquina y permite que Spring procese anticipadamente parte de la configuración. Añado el plugin de graalvm native al
  build.gradle. Ahora, para ejecutar naviteCompile, añado el nombre del ejecutable al build.gradle:

```gradle
graalvmNative {
    binaries {
        main {
            imageName = 'product-api'
        }
    }
}
```

- Ahora necesito modificar el Dockerfile para que no me cree un JAR y lo ejecute con la JVM. Modifico el Dockerfile:

```dockerfile
RUN ./gradlew nativeCompile --no-daemon

ENTRYPOINT ["/app/product-api"]
```

- Construyo la imagen: docker compose build

![img.png](img/img.png)

![img2.png](img/img2.png)

![img3.png](img/img3.png)

TIEMPO DE ARRANQUE CON OPTIMIZACIONES: Root WebApplicationContext: initialization completed in 37 ms

![img4.png](img/img4.png)

### Optimizar tiempo de las queries

- Añado índice para mejorar el rendimiento de las queries en el esquema sql de Flyway:

```sql
CREATE INDEX IDX_PRICE_PRODUCT_DATES
    ON PRICE (PRODUCT_ID, INIT_DATE, END_DATE);
```

### Ejecuto el benchmark.sh que venía con el proyecto para ver cuanto tardan las ejecuciones

Añado a Dockerfile.benchmark el bc para poder ver lo que dura la ejecución:

```text
DURATION=$(echo "$END_TIME - $START_TIME" | bc)
```

- Para tener controlada la prueba, arranco primero el contenedor de oracle y de la aplicación: docker compose up -d
  oracle app. Una vez levantados, ejecuto el de benchmark.

TIEMPO EN REALIZARSE TODAS LAS PETICIONES CON EL SCRIPT BENCHMARK INICIAL:

![img6.png](img/img6.png)

1000 peticiones POST "$BASE_URL/products": 6.720584981 seconds

20000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/price?date=2024-04-15": 252.118104199 seconds

15000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/prices": 283.480768207 seconds

### Creación de un nuevo contenedor que ejecuta múltiples peticiones concurrentes

Elijo usar k6 porque a parte de lanzar varias peticiones concurrentes, me aporta métricas.

Me decido por la imagen Docker grafana/k6:latest: https://hub.docker.com/r/grafana/k6

En el script benchmark.sh que se adjunta con el proyecto veo que se dan:

```text
1000 peticiones POST "$BASE_URL/products"
20000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/price?date=2024-04-15"
15000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/prices"
```

Trato de recrear que se lancen las mismas peticiones en mi archivo `benchmark.js`: me parece más realista que hayan
distintos usuarios lanzando las peticiones, no solo uno.

- product_creation: 25 usuarios virtuales lanzarán en total 1000 peticiones
- price_by_date: 50 usuarios virtuales lanzarán un total de 20000 peticiones
- price_history: 50 usuarios virtuales lanzarán un total de 15000 peticiones

![img7.png](img/img7.png)

- Ejecuto el contenedor de benchmark k6 que he creado: docker compose run --rm --no-deps benchmark

![img8.png](img/img8.png)
![img9.png](img/img9.png)

1000 peticiones POST "$BASE_URL/products":

20000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/price?date=2024-04-15":

15000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/prices":

## 5. Resultados de la ejecución

### Velocidad de arranque de la aplicación

- Velocidad inicial de la app: Root WebApplicationContext: initialization completed in 12095 ms
- Velocidad tras la optimización: Root WebApplicationContext: initialization completed in 37 ms

### Velocidad de ejecución de los endpoints

- Velocidad inicial ejecución benchmark.sh:

1000 peticiones POST "$BASE_URL/products": 6.720584981 seconds

20000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/price?date=2024-04-15": 252.118104199 seconds

15000 peticiones GET "$BASE_URL/products/$PRODUCT_ID/prices": 283.480768207 seconds

- Velocidad tras la optimización con k6:

1000 peticiones k6 POST "$BASE_URL/products": 8 seconds

20000 peticiones k6 GET "$BASE_URL/products/$PRODUCT_ID/price?date=2024-04-15": 42 seconds

15000 peticiones k6 GET "$BASE_URL/products/$PRODUCT_ID/prices": 34,3 seconds

### Peticiones exitosas por segundo

- Con k6: Como no ha habido ningún error: http_reqs........: 36003 854.72035/s

![img10.png](img/img10.png)

### Uso de recursos bajo carga

- Ejecuto docker stats product-api mangodb, y luego docker compose run --rm --no-deps benchmark:

## 6. Entrega

### Zip del proyecto que contiene el archivo docker-compose.yml

Se entrega el zip con el proyecto que contiene el archivo docker-compose.yml que levanta la aplicación, oracle y
benchmark para que se envíen múltiples peticiones concurrentes.
