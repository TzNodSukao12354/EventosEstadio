# Semana 17: Mapa de Asientos Visuales

## ¿Qué aprendimos esta semana?

Esta semana implementamos la funcionalidad más avanzada del proyecto: un mapa visual de asientos tipo cine donde podés ver y seleccionar tu asiento antes de comprar.

## ¿Qué es el mapa de asientos?

El mapa de asientos es como cuando comprás entradas para el cine: ves un plano del estadio con todos los asientos, los verdes son disponibles, los rojos están ocupados, y hacés clic en uno para seleccionarlo.

```
Sin mapa de asientos:
Usuario → Elige zona → Compra → El sistema le asigna un asiento al azar

Con mapa de asientos:
Usuario → Elige zona → Ve el mapa → Elige su asiento → Compra
```

## ¿Qué hicimos?

### Backend

#### 1. Creamos `AsientoEvento.java` (Model)

Este model representa un asiento específico en un evento:

| Campo | ¿Qué es? |
|-------|---------|
| `eventoId` | El evento al que pertenece el asiento |
| `zonaId` | La zona donde está el asiento |
| `fila` | La fila del asiento (A, B, C...) |
| `columna` | La columna del asiento (1, 2, 3...) |
| `estado` | El estado: DISPONIBLE, OCUPADO, RESERVADO |
| `documentoUsuario` | El DNI de quien compró el asiento (si está ocupado) |

#### 2. Creamos `AsientoEventoRepository.java`

Este repository busca asientos en la base de datos:

| Método | ¿Qué hace? |
|--------|-----------|
| `findByEventoIdAndZonaId()` | Obtiene todos los asientos de una zona de un evento |
| `countByEventoIdAndZonaIdAndEstado()` | Cuenta cuántos asientos hay por estado |
| `findByEventoIdAndZonaIdAndFilaAndColumna()` | Busca un asiento específico |

#### 3. Creamos `AsientoEventoService.java`

Este service genera los asientos automáticamente:

| Método | ¿Qué hace? |
|--------|-----------|
| `generarAsientos(eventoId, zonaId)` | Crea los asientos según el aforo de la zona |
| `reservarAsiento()` | Marca un asiento como reservado |
| `liberarAsiento()` | Libera un asiento reservado |

**¿Cómo se generan los asientos?**
- Estadios grandes (más de 10,000 capacidad): 50 columnas × N filas
- Arenas pequeñas: 15 columnas × N filas
- Las filas se calculan dividiendo el aforo entre la cantidad de columnas

#### 4. Creamos `AsientoController.java`

| Endpoint | ¿Qué hace? |
|----------|-----------|
| `GET /api/asientos/evento/{id}/zona/{id}` | Obtiene el mapa de asientos de una zona |
| `POST /api/asientos/reservar` | Reserva un asiento específico |
| `POST /api/asientos/liberar` | Libera un asiento reservado |

### Frontend

#### 5. Creamos `mapa-asientos/` (Componente)

| Archivo | ¿Qué hace? |
|---------|-----------|
| `mapa-asientos.ts` | Carga el mapa desde el backend y emite eventos cuando seleccionás un asiento |
| `mapa-asientos.html` | Muestra el escenario, las filas de asientos, y la leyenda de colores |
| `mapa-asientos.css` | Estilos: colores verde (disponible), rojo (ocupado), azul (seleccionado) |

**¿Qué ves en el mapa?**
```
        PISTA / CANCHA / ESCENARIO

    [A1][A2][A3][A4][A5]...[A50]
    [B1][B2][B3][B4][B5]...[B50]
    [C1][C2][C3][C4][C5]...[C50]
    ...

    🟢 Disponible    🔴 Ocupado    🔵 Seleccionado
```

#### 6. Integración con `comprar-entrada/`

El componente de compra de entradas ahora incluye el mapa de asientos:

1. Elegís la zona
2. Se carga el mapa de asientos de esa zona
3. Hacés clic en un asiento verde
4. El asiento se marca en azul (seleccionado)
5. Hacés clic en "Comprar" y el asiento se reserva

## ¿Dónde está el código de esta semana?

**Backend:**
- `src/main/java/com/example/demo/model/AsientoEvento.java`
- `src/main/java/com/example/demo/repository/AsientoEventoRepository.java`
- `src/main/java/com/example/demo/service/AsientoEventoService.java`
- `src/main/java/com/example/demo/controller/AsientoController.java`

**Frontend:**
- `boletos-frontend/src/app/components/mapa-asientos/mapa-asientos.ts`
- `boletos-frontend/src/app/components/mapa-asientos/mapa-asientos.html`
- `boletos-frontend/src/app/components/mapa-asientos/mapa-asientos.css`

## Flujo completo de compra con mapa

```
1. Usuario ve la lista de eventos
2. Elegís un evento y hacé clic en "Comprar"
3. Se abre el formulario de compra
4. Elegís la zona (Platea, Grada, VIP)
5. El backend genera los asientos si no existen
6. El mapa se muestra con los asientos disponibles (verde) y ocupados (rojo)
7. Hacé clic en un asiento verde → Se marca en azul
8. Elegís tipo de entrega (E-Ticket o Física)
9. Hacé clic en "Comprar"
10. El backend reserva el asiento y crea la entrada
11. La entrada aparece en "Mis Entradas"
```

## Frase clave para defender

> "Implementamos un sistema de selección visual de asientos similar a las plataformas de venta de entradas comerciales, donde el usuario puede ver el plano del estadio, identificar los asientos disponibles y ocupados, y seleccionar su asiento específico antes de completar la compra."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Mapa de asientos | Plano visual del estadio con todos los asientos |
| Asiento | Un lugar específico identificado por fila y columna |
| Estado del asiento | DISPONIBLE (verde), OCUPADO (rojo), RESERVADO (azul) |
| Generación automática | Los asientos se crean según el aforo de la zona |
| Reserva | Marcar un asiento como reservado antes de confirmar la compra |
| Componente hijo | mapa-asientos se usa dentro de comprar-entrada |
| @Input() | Recibe datos del padre (eventoId, zonaId) |
| @Output() | Envía datos al padre (asiento seleccionado) |
