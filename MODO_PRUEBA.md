# Modo de Prueba - Base de Datos Temporal

## Que es esto

Tu proyecto usa **H2 en memoria** como base de datos. Esto significa que **todo lo que hagas de prueba se borra cuando apagás el backend**. No necesitás MySQL ni ninguna base de datos externa para probar.

## Como funciona

### Cada vez que encendés el backend:

1. **Se crea una base de datos nueva** en la memoria de tu computadora.
2. **Se ejecuta `data.sql`** automáticamente, que carga los datos de prueba (estadios, zonas, eventos, usuarios).
3. **Todo está listo** para que probés el sistema.

### Cuando apagás el backend:

- **La base de datos desaparece** completamente.
- Los estadios, zonas, eventos, entradas compradas, etc. **se borran**.
- La próxima vez que encendás el backend, todo vuelve a cargar desde cero con `data.sql`.

## Datos de prueba que se cargan

### Usuarios

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | Administrador |
| user | user123 | Usuario normal |

### Estadios

1. **Estadio Nacional** - 50,000 capacidad
2. **Arena Lima** - 15,000 capacidad
3. **Alejandro Villanueva** - 35,000 capacidad
4. **Jockey Plaza Park** - 30,000 capacidad (para BTS)

### Eventos

1. **Alianza Lima vs Universitario** - Final de fútbol (ESTADIO)
2. **Concierto Bad Bunny** - Most Wanted Tour (ARENA)
3. **BTS World Tour Lima 2026** - Concierto circular (CIRCULAR)
4. Y más eventos...

### Zonas por evento

Cada evento tiene sus zonas con diferente formación:
- **ESTADIO**: Grad as rectangulares con curvatura (fútbol)
- **CIRCULAR**: Pista en el centro con anillos concéntricos (BTS)
- **ARENA**: Escenario en frente con semicírculo (Bad Bunny)

## Como encender todo

### Terminal 1: Backend

```
mvn clean package -DskipTests
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

El backend va a arrancar en **http://localhost:8080/**

### Terminal 2: Frontend

```
cd boletos-frontend
npx ng serve
```

El frontend va a arrancar en **http://localhost:4200/**

### Abrir el navegador

Andá a **http://localhost:4200/** y logueate con `admin` / `admin123`.

## Que podés probar

### 1. Comprar entradas

- Logueate como `user` / `user123`
- Andá a la lista de eventos
- Elegí un evento y hacé clic en "Comprar"
- Elegí una zona y seleccioná un asiento en el mapa
- Comprá la entrada

### 2. Ver mis entradas

- Después de comprar, andá a "Mis Entradas"
- Vas a ver la entrada que compraste con el número de asiento

### 3. Panel de admin

- Logueate como `admin` / `admin123`
- Andá al panel de administración
- Podés ver los eventos, estadios, zonas y entradas

### 4. Mapa de asientos

- Cada evento tiene un mapa diferente según su formación
- **Fútbol**: Grad as rectangulares
- **BTS**: Pista circular en el centro
- **Bad Bunny**: Escenario en frente

## Importante

### Lo que NO se guarda en MySQL

- **Nada**. Todo está en H2 en memoria.
- Si apagás el backend, **se borra todo**.
- Si comprás 100 entradas y reiniciás, **vuelven a estar disponibles**.
- Esto es perfecto para probar sin miedo a romper nada.

### Si querés usar MySQL en el futuro

Si algún día querés usar MySQL para producción, solo tenés que cambiar `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/boletos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

Por ahora, **no lo tocés**. H2 en memoria es perfecto para pruebas.

## Para defender el proyecto

> "Usamos H2 en memoria como base de datos para el entorno de desarrollo y pruebas. Esto nos permite tener un entorno de prueba limpio cada vez que reiniciamos la aplicación, sin depender de MySQL ni contaminar la base de datos de producción. Los datos de prueba se cargan automáticamente desde `data.sql` al iniciar la aplicación."
