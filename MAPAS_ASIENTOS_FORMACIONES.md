# Mapas de Asientos - Formaciones

## Que es esto

El sistema de boletos ahora puede mostrar el mapa de asientos de 3 formas diferentes, dependiendo del tipo de evento. Antes solo tenías filas rectas como un cine. Ahora el mapa se adapta al tipo de escenario.

---

## Las 3 Formaciones

### 1. ESTADIO (Fútbol)

**Como se ve:**
- Las gradas son rectangulares con una curva suave.
- La cancha/pista está arriba, como una barra horizontal.
- Los asientos están organizados en filas y columnas, como siempre.

**Para que sirve:**
- Partidos de fútbol
- Eventos donde la cancha es rectangular

**Prueba con:**
- **Evento:** Alianza Lima vs Universitario - Final
- **Estadio:** Alejandro Villanueva
- **Zonas:** Platea, Categoría 1, Categoría 2, Popular

**Como probarlo:**
1. Logueate con `admin` / `admin123`
2. Buscá el evento "Alianza Lima vs Universitario"
3. Hacé clic en "Comprar"
4. Elegí una zona (Platea, Categoría 1, etc.)
5. Vas a ver el mapa con forma de estadio

---

### 2. CIRCULAR (BTS - Pista en el centro)

**Como se ve:**
- Hay una **pista circular en el centro** del mapa.
- Los asientos rodean la pista en **anillos concéntricos** (círculos dentro de círculos).
- Es como ver un estadio desde arriba, con el escenario en el medio.

**Para que sirve:**
- Conciertos donde el artista está en el centro
- Eventos tipo "360 grados" como BTS, Ed Sheeran, etc.

**Prueba con:**
- **Evento:** BTS - World Tour Lima 2026
- **Estadio:** Jockey Plaza Park
- **Zonas:** Pista Central, Grada Frontal, Grada Lateral, Grada Superior

**Como probarlo:**
1. Logueate con `admin` / `admin123`
2. Buscá el evento "BTS - World Tour Lima 2026"
3. Hacé clic en "Comprar"
4. Elegí una zona
5. Vas a ver la pista circular en el centro con los asientos rodeándola

---

### 3. ARENA (Bad Bunny - Escenario en frente)

**Como se ve:**
- El **escenario está abajo** (en frente).
- Los asientos están organizados en **semicírculo** alrededor del escenario.
- Es como un teatro grande: el artista está al frente y el público lo mira de frente.

**Para que sirve:**
- Conciertos en arenas cerradas
- Shows donde el artista está en un escenario fijo al frente

**Prueba con:**
- **Evento:** Concierto Bad Bunny - Most Wanted Tour
- **Estadio:** Arena Lima
- **Zonas:** Gold, Silver, Bronce

**Como probarlo:**
1. Logueate con `admin` / `admin123`
2. Buscá el evento "Bad Bunny - Most Wanted Tour"
3. Hacé clic en "Comprar"
4. Elegí una zona (Gold, Silver, Bronce)
5. Vas a ver el escenario abajo y los asientos en semicírculo arriba

---

## Como encender todo para probar

### Paso 1: Backend (Spring Boot)

Abrí una terminal en la carpeta del proyecto y ejecutá:

```
mvn clean package -DskipTests
```

Después:

```
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

El backend va a arrancar en **http://localhost:8080/**

### Paso 2: Frontend (Angular)

Abrí OTRA terminal en la carpeta `boletos-frontend` y ejecutá:

```
cd boletos-frontend
npx ng serve
```

El frontend va a arrancar en **http://localhost:4200/**

### Paso 3: Abrir el navegador

Andá a **http://localhost:4200/** y logueate con:
- Usuario: `admin` / Contraseña: `admin123`

---

## Que archivos cambiaron

### Backend (Java)

| Archivo | Que hace |
|---|---|
| `Zona.java` | Agregué el campo `formacion` (ESTADIO, CIRCULAR, ARENA) |
| `AsientoEventoService.java` | Ahora devuelve la formación en el JSON del mapa |
| `data.sql` | Agregué el estadio Jockey Plaza Park, las zonas de BTS y las formaciones a todas las zonas |

### Frontend (Angular)

| Archivo | Que hace |
|---|---|
| `mapa-asientos.ts` | 3 métodos de posición: `calcularPosicionesEstadio()`, `calcularPosicionesCircular()`, `calcularPosicionesArena()`. También dibuja el escenario diferente según la formación |
| `mapa-asientos.html` | Muestra un badge con el tipo de formación |
| `mapa-asientos.css` | Colores para los badges: azul=ESTADIO, morado=CIRCULAR, rojo=ARENA |

---

## Como se ve cada uno

### ESTADIO
```
        [  CANCHA / PISTA  ]

    [A1][A2][A3][A4][A5]
    [B1][B2][B3][B4][B5]
    [C1][C2][C3][C4][C5]
    [D1][D2][D3][D4][D5]
```

### CIRCULAR (BTS)
```
          [Asientos]
      [As]       [As]
    [As]   PISTA   [As]
      [As] CENTRAL [As]
          [Asientos]
```

### ARENA (Bad Bunny)
```
    [As][As][As][As][As]
   [As]              [As]
   [As]              [As]
        [ ESCENARIO ]
```

---

## Para defender el proyecto

> "Implementamos un sistema de mapas de asientos que se adapta al tipo de evento. Usando Canvas API en Angular, dibujamos los asientos con 3 formaciones diferentes: ESTADIO (gradas rectangulares con curvatura), CIRCULAR (pista en el centro con anillos concéntricos) y ARENA (escenario en frente con semicírculo). La formación se guarda en la base de datos y el backend la envía al frontend, que decide cómo dibujar el mapa."
