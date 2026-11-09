# Semana 4: API REST

## ¿Qué aprendimos esta semana?

Esta semana aprendimos qué es REST y cómo nuestra aplicación cumple con los principios de una API RESTful.

## ¿Qué es REST?

REST significa "Representational State Transfer". Suena complicado, pero en realidad son reglas simples para que el frontend y el backend se comuniquen de forma ordenada.

Imaginate que REST es como el protocolo de una conversación:
- Vos pedís algo con palabras claras
- El otro te responde con lo que pediste
- No necesitás recordar lo que hablaste antes (sin estado)

## Los 6 principios de REST

| Principio | ¿Qué significa? | ¿Cómo lo aplicamos? |
|-----------|----------------|---------------------|
| **Cliente-Servidor** | Separar lo que ves de lo que procesa | Frontend (Angular) separado del Backend (Spring Boot) |
| **Sin estado (Stateless)** | Cada petición es independiente | Usamos JWT, no guardamos sesión en el servidor |
| **Caché** | Se puede guardar la respuesta para no pedirlo de nuevo | Las respuestas de GET se pueden cachear |
| **Capas** | Podés agregar intermediarios (load balancer, proxy) | Nuestro backend puede tener un servidor intermedio |
| **Código bajo demanda** | El cliente puede descargar código para ejecutarse | No usamos esto, pero es parte de REST |
| **Interfaz uniforme** | Todas las peticiones siguen el mismo formato | Usamos los mismos métodos HTTP para todo |

## Métodos HTTP para CRUD

| Método | Acción | Ejemplo en nuestro proyecto |
|--------|--------|----------------------------|
| **GET** | Leer | `GET /eventos` → Lista todos los eventos |
| **POST** | Crear | `POST /entradas` → Compra una nueva entrada |
| **PUT** | Actualizar | `PUT /eventos/1` → Actualiza el evento 1 |
| **DELETE** | Borrar | `DELETE /entradas/5` → Cancela la entrada 5 |

## Códigos de respuesta HTTP

| Código | Significado | Ejemplo |
|--------|------------|---------|
| 200 | OK, todo bien | Se obtuvo la lista de eventos |
| 201 | Creado | Se creó un nuevo evento |
| 400 | Error del cliente | Datos inválidos |
| 401 | No autorizado | No tenés token o el token es inválido |
| 403 | Prohibido | No tenés permiso (ej: usuario intenta acceder a admin) |
| 404 | No encontrado | El evento que buscaste no existe |
| 500 | Error del servidor | Algo falló en el backend |

## ¿Cómo funciona una petición completa?

```
Frontend (Angular)                          Backend (Spring Boot)
      |                                           |
      |  GET /eventos (petición)                  |
      | ----------------------------------------> |
      |                                           |  EventoController recibe la petición
      |                                           |  EventoService busca los eventos
      |                                           |  EventoRepository consulta la BD
      |                                           |
      |                                           |  200 OK + lista de eventos (respuesta)
      | <---------------------------------------- |
      |                                           |
      |  Muestra los eventos en pantalla          |
```

## ¿Dónde está el código de esta semana?

Todo el código de los controllers de la Semana 2 ya es API REST. Los archivos son:

- `src/main/java/com/example/demo/controller/EventoController.java`
- `src/main/java/com/example/demo/controller/EstadioController.java`
- `src/main/java/com/example/demo/controller/ZonaController.java`
- `src/main/java/com/example/demo/controller/EntradaController.java`
- `src/main/java/com/example/demo/controller/AuthController.java`
- `src/main/java/com/example/demo/controller/AsientoController.java`

## Frase clave para defender

> "Nuestra API cumple con los principios RESTful: es sin estado (stateless) gracias al JWT, y utiliza los métodos HTTP estándar (GET, POST, PUT, DELETE) para manipular los recursos de entradas y eventos de forma predecible."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| REST | Reglas para que frontend y backend se comuniquen |
| Stateless | Cada petición es independiente, no hay sesión |
| CRUD | Crear, Leer, Actualizar, Borrar |
| GET | Leer datos |
| POST | Crear datos |
| PUT | Actualizar datos |
| DELETE | Borrar datos |
| 200 OK | Todo salió bien |
| 401 Unauthorized | No tenés permiso |
| 404 Not Found | No se encontró lo que buscabas |
