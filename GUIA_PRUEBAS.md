# Guía de Pruebas - Sistema de Boletos

## Que tienes que prender

### Paso 1: Prender el Backend (Spring Boot)

Abri una terminal en la carpeta del proyecto y ejecuta:

```
mvn clean package -DskipTests
```

Después ejecuta:

```
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

El backend va a arrancar en **http://localhost:8080/**

### Paso 2: Prender el Frontend (Angular)

Abri OTRA terminal en la carpeta `boletos-frontend` y ejecuta:

```
cd boletos-frontend
npx ng serve
```

El frontend va a arrancar en **http://localhost:4200/**

### Paso 3: Abrir el navegador

Anda a **http://localhost:4200/** y logueate con:
- Usuario: `admin` / Contraseña: `admin123`

---

## Los 3 Escenarios de Prueba

### Prueba 1: Estadio de Futbol (Formacion ESTADIO)

**Evento:** Peru vs Brasil - Eliminatorias

**Como probarlo:**
1. Logueate en el sistema
2. Busca el evento "Peru vs Brasil" en la lista
3. Hacé clic en "Comprar"
4. Elegi una zona (Platea Preferencia, Grada, etc.)
5. Vas a ver el mapa de asientos en **filas rectangulares con curvatura**
6. El escenario/cancha esta ARRIBA y las gradas estan ABAJO
7. Un badge azul dira "ESTADIO"

**Que ves:**
- Asientos organizados en filas y columnas
- La cancha esta arriba
- Las gradas tienen una curva suave
- Es como un estadio de futbol normal

---

### Prueba 2: BTS - Formacion Circular (Formacion CIRCULAR)

**Evento:** BTS - World Tour Lima 2026

**Como probarlo:**
1. Logueate en el sistema
2. Busca el evento "BTS - World Tour Lima 2026"
3. Hacé clic en "Comprar"
4. Elegi una zona (Pista Central, Grada Frontal, etc.)
5. Vas a ver el mapa de asientos en **anillos concéntricos**
6. La pista central es un **circulo en el medio**
7. Un badge morado dira "CIRCULAR"

**Que ves:**
- Un circulo gris en el centro que dice "PISTA CENTRAL"
- Los asientos estan distribuidos en arcos alrededor de la pista
- Es como cuando el artista esta en el medio del estadio y las gradas lo rodean
- Las filas son anillos que se alejan del centro

**Zonas disponibles:**
- Pista Central - S/400 (mas cerca del escenario)
- Grada Frontal - S/250
- Grada Lateral - S/180
- Grada Superior - S/100

---

### Prueba 3: Bad Bunny - Formacion Arena (Formacion ARENA)

**Evento:** Concierto Bad Bunny - Most Wanted Tour

**Como probarlo:**
1. Logueate en el sistema
2. Busca el evento "Bad Bunny - Most Wanted Tour"
3. Hacé clic en "Comprar"
4. Elegi una zona (Gold, Silver, Bronce)
5. Vas a ver el mapa de asientos en **semicirculo**
6. El escenario esta ABAJO (en frente)
7. Un badge rojo dira "ARENA"

**Que ves:**
- Un rectangulo rojo abajo que dice "ESCENARIO"
- Los asientos estan distribuidos en un semicirculo arriba del escenario
- Es como un concierto donde el artista esta en un escenario al frente
- Las gradas rodean de frente a los lados

**Zonas disponibles:**
- Gold - S/300 (mas cerca del escenario)
- Silver - S/180
- Bronce - S/100

---

## Como funciona el mapa de asientos

### Colores
- **Verde** = Asiento disponible (podes comprarlo)
- **Rojo** = Asiento ocupado (ya vendido)
- **Azul** = Asiento que seleccionaste

### Controles
- **Rueda del mouse** = Zoom (acercar o alejar)
- **Arrastrar** = Mover el mapa
- **Click en asiento verde** = Seleccionar ese asiento
- **Boton Reset Zoom** = Volver al tamaño original

---

## Resumen de los 3 tipos de formacion

| Tipo | Donde esta el escenario | Como se ven los asientos | Ejemplo |
|------|------------------------|-------------------------|---------|
| ESTADIO | Arriba (cancha) | Filas rectangulares con curva | Futbol, Peru vs Brasil |
| CIRCULAR | En el centro (circulo) | Anillos alrededor | BTS, conciertos 360° |
| ARENA | Abajo (frente) | Semicirculo arriba | Bad Bunny, conciertos normales |

---

## Preguntas frecuentes

### Porque hay 3 tipos de formacion?

Porque cada evento se organiza diferente:
- En un partido de futbol, la cancha esta en el medio y las gradas rodean
- En un concierto de BTS, el escenario esta en el centro y las gradas lo rodean
- En un concierto de Bad Bunny, el escenario esta al frente y las gradas estan arriba

### Quien crea estos eventos?

En la vida real, la empresa del estadio o el organizador del evento proporciona:
- El plano del lugar
- Cuantos asientos hay en cada zona
- Cuanto cuesta cada zona
- Como se organiza el escenario

En este sistema, nosotros creamos los eventos en la base de datos (`data.sql`) con esa informacion.

### Como agrego un nuevo evento?

Edita el archivo `src/main/resources/data.sql` y agregá:
1. El evento en la tabla `eventos`
2. Las zonas en la tabla `zonas` (si es un estadio nuevo)
3. La disponibilidad en `disponibilidad_zona`
4. Elegi la formacion: ESTADIO, CIRCULAR, o ARENA
