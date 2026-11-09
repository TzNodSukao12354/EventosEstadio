# Semana 6: Spring Data JPA y Base de Datos

## ¿Qué aprendimos esta semana?

Esta semana aprendimos cómo guardar y buscar datos en una base de datos usando Spring Data JPA.

## ¿Qué es una base de datos?

Una base de datos es como una alacena donde guardás información. En vez de guardar los datos en variables (que se borran cuando apagás el programa), los guardás en la base de datos para que persistan.

## ¿Qué es JPA?

JPA (Java Persistence API) es como un traductor. Vos escribís código Java, y JPA lo traduce a SQL (el lenguaje de las bases de datos).

```
Vos (Java) → JPA → Base de datos (SQL)
```

En vez de escribir:
```sql
SELECT * FROM eventos WHERE id = 1
```

Escribís:
```java
eventoRepository.findById(1L)
```

## ¿Qué hicimos?

### 1. Creamos los Models (Entidades)

Los models son como los planos de las tablas de la base de datos. Cada model es una tabla.

| Model | Tabla | ¿Qué guarda? |
|-------|-------|-------------|
| `Evento.java` | `evento` | Nombre, fecha, hora, estadio, tipo |
| `Estadio.java` | `estadio` | Nombre, ciudad, capacidad |
| `Zona.java` | `zona` | Nombre, precio, aforo máximo |
| `Entrada.java` | `entrada` | Evento, zona, precio, estado, usuario |
| `AsientoEvento.java` | `asiento_evento` | Fila, columna, estado, evento, zona |
| `DisponibilidadZona.java` | `disponibilidad_zona` | Cuántos asientos quedan por zona |

### 2. Creamos los Repositories

Los repositories son como los buscadores. En vez de escribir SQL, Spring Data JPA genera las consultas automáticamente.

```java
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Spring genera automáticamente: findAll, findById, save, delete
    // También podés agregar métodos personalizados:
    List<Evento> findByTipoEvento(String tipo);
}
```

### 3. Configuramos H2 (Base de datos en memoria)

Usamos H2 como base de datos. H2 es una base de datos que vive en la memoria de tu computadora. Es perfecta para desarrollo porque no necesitás instalar nada.

Configuración en `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### 4. Datos de prueba (`data.sql`)

Creamos un archivo `data.sql` que carga datos de prueba cada vez que arrancamos la aplicación:

- Estadios: Maracaná, Nacional, etc.
- Zonas: Platea, Grada, VIP
- Eventos: Conciertos, partidos de fútbol
- Usuarios: admin y user con contraseñas

## ¿Dónde está el código de esta semana?

- `src/main/java/com/example/demo/model/` → Las entidades (tablas)
- `src/main/java/com/example/demo/repository/` → Los repositories (buscadores)
- `src/main/resources/application.properties` → Configuración de la base de datos
- `src/main/resources/data.sql` → Datos de prueba

## Frase clave para defender

> "Usamos Spring Data JPA para interactuar con la base de datos de forma sencilla, definiendo entidades con JPA y repositories que generan automáticamente las consultas SQL."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Base de datos | Alacena donde guardás información |
| JPA | Traductor de Java a SQL |
| Entity | Un plano de una tabla |
| Repository | Un buscador que genera consultas automáticamente |
| H2 | Base de datos en memoria para desarrollo |
| @Id | El identificador único de cada registro |
| @Table | El nombre de la tabla en la base de datos |
| JpaRepository | Interfaz que da métodos listos para usar |
