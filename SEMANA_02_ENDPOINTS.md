# Semana 2: Endpoints y Controladores

## ¿Qué aprendimos esta semana?

Esta semana aprendimos cómo crear endpoints (rutas) en nuestra aplicación. Un endpoint es como una dirección donde el frontend puede pedir información.

## ¿Qué es un endpoint?

Un endpoint es como un número de teléfono. Cuando el frontend quiere algo, "llama" a un endpoint y el backend le responde.

Por ejemplo:
- `GET /eventos` → "Dame la lista de eventos"
- `POST /entradas` → "Creá una nueva entrada"
- `PUT /eventos/1` → "Actualizá el evento número 1"
- `DELETE /entradas/5` → "Borrá la entrada número 5"

## ¿Qué hicimos?

### 1. Creamos los Controllers

Los controllers son como los meseros de un restaurante. Reciben la petición del cliente y se la pasan a la cocina (service).

Usamos anotaciones especiales de Spring Boot:

| Anotación | ¿Qué hace? |
|-----------|-----------|
| `@RestController` | Dice "esta clase es un controller que responde peticiones web" |
| `@GetMapping` | Responde a peticiones GET (leer datos) |
| `@PostMapping` | Responde a peticiones POST (crear datos) |
| `@PutMapping` | Responde a peticiones PUT (actualizar datos) |
| `@DeleteMapping` | Responde a peticiones DELETE (borrar datos) |

### 2. Nuestros Controllers

#### `AuthController.java`
- `POST /auth/login` → Recibe usuario y contraseña, devuelve un token

#### `EventoController.java`
- `GET /eventos` → Lista todos los eventos
- `GET /eventos/{id}` → Busca un evento por ID
- `POST /eventos` → Crea un nuevo evento
- `PUT /eventos/{id}` → Actualiza un evento
- `DELETE /eventos/{id}` → Borra un evento

#### `EstadioController.java`
- `GET /estadios` → Lista todos los estadios
- `POST /estadios` → Crea un nuevo estadio
- `PUT /estadios/{id}` → Actualiza un estadio
- `DELETE /estadios/{id}` → Borra un estadio

#### `ZonaController.java`
- `GET /zonas` → Lista todas las zonas
- `POST /zonas` → Crea una nueva zona
- `PUT /zonas/{id}` → Actualiza una zona
- `DELETE /zonas/{id}` → Borra una zona

#### `EntradaController.java`
- `GET /entradas` → Lista las entradas
- `POST /entradas` → Compra una nueva entrada
- `PUT /entradas/{id}/reportar-perdida` → Reporta una entrada perdida
- `POST /entradas/{id}/reimpresion` → Reimprime una entrada dañada
- `PUT /entradas/{id}/marcar-usada` → Marca una entrada como usada
- `DELETE /entradas/{id}` → Cancela una entrada

#### `AsientoController.java`
- `GET /api/asientos/evento/{id}/zona/{id}` → Obtiene el mapa de asientos
- `POST /api/asientos/reservar` → Reserva un asiento específico
- `POST /api/asientos/liberar` → Libera un asiento reservado

## Ejemplo de código

```java
@RestController
public class EventoController {

    @GetMapping("/eventos")
    public List<Evento> listarEventos() {
        // Devuelve todos los eventos
    }

    @PostMapping("/eventos")
    public Evento crearEvento(@RequestBody Evento evento) {
        // Crea un nuevo evento
    }
}
```

## ¿Dónde está el código de esta semana?

- `src/main/java/com/example/demo/controller/` → Todos los controllers

## Frase clave para defender

> "Definimos endpoints RESTful claros usando @RestController y los mapeos HTTP (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping) para realizar operaciones CRUD sobre los recursos del estadio."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Endpoint | Una dirección donde el frontend pide datos |
| Controller | El mesero que recibe la petición |
| GET | Leer datos |
| POST | Crear datos |
| PUT | Actualizar datos |
| DELETE | Borrar datos |
| @RestController | Anotación que convierte una clase en controller |
