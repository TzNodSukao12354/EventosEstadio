# 🏟️ Sistema de Boletos para Estadio - Explicación Simple

## ¿De qué trata este proyecto?

Imaginate que querés comprar una entrada para un concierto o un partido de fútbol. Este proyecto es exactamente eso: **una página web donde podés comprar entradas para eventos en un estadio**.

Es como cuando comprás entradas en Ticketmaster o Entradas.com, pero lo hicimos nosotros desde cero.

---

## ¿Cómo está organizado todo?

El proyecto tiene dos partes principales, como si fuera una restaurante:

1. **El Backend (cocina)** → Spring Boot (Java) - Es donde se procesa todo
2. **El Frontend (mesa)** → Angular (TypeScript) - Es lo que ves en la pantalla

```
Estadio_proyecto/
│
├── src/main/java/          ← BACKEND (la cocina)
│   └── com/example/demo/
│       ├── controller/     ← Los meseros (reciben las órdenes)
│       ├── service/        ← Los cocineros (hacen el trabajo real)
│       ├── repository/     ← Los que van a la alacena (buscan datos)
│       ├── model/          ← Los platos (qué son los datos)
│       ├── config/         ← Las reglas del restaurante
│       └── filter/         ← El seguridad (revisa quién entra)
│
├── boletos-frontend/       ← FRONTEND (la mesa)
│   └── src/app/
│       ├── components/     ← Las pantallas que ves
│       ├── services/       ← Los que hablan con el backend
│       └── interceptors/   ← El que lleva tu token de acceso
│
└── pom.xml                 ← La lista de ingredientes (dependencias)
```

---

## Backend (Spring Boot - Java)

### ¿Qué es el Backend?

El backend es como la cocina de un restaurante. Vos no lo ves, pero ahí es donde se hace todo el trabajo. Cuando hacés clic en "Comprar entrada", el backend es el que verifica si hay asientos disponibles, guarda tu compra, y te dice si funcionó o no.

### Las carpetas del Backend

#### 📁 `controller/` - Los Meseros

Los controllers son como los meseros del restaurante. Reciben tu pedido (por ejemplo, "quiero comprar una entrada") y se lo pasan a la cocina (service).

| Archivo | ¿Qué hace? |
|---------|-----------|
| `AuthController.java` | Recibe tu usuario y contraseña. Si son correctos, te da un token (como un pase de acceso) |
| `EventoController.java` | Te muestra la lista de eventos, crea nuevos eventos, o borra eventos |
| `EstadioController.java` | Lo mismo pero para estadios (Maracaná, Nacional, etc.) |
| `ZonaController.java` | Gestiona las zonas del estadio (Platea, Grada, VIP) |
| `EntradaController.java` | Gestiona las entradas: compra, cancela, reporta pérdida |
| `AsientoController.java` | Gestiona los asientos individuales del mapa visual |

#### 📁 `service/` - Los Cocineros

Los services son los que hacen el trabajo real. El mesero (controller) les dice "prepará este pedido" y ellos lo hacen.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `EventoService.java` | Crea, busca, actualiza y borra eventos |
| `EstadioService.java` | Lo mismo para estadios |
| `ZonaService.java` | Lo mismo para zonas |
| `EntradaService.java` | Verifica si podés comprar (¿hay aforo? ¿el evento no pasó?) y procesa la compra |
| `EmailService.java` | Envía correos cuando comprás una entrada |
| `AsientoEventoService.java` | Genera el mapa de asientos y gestiona la reserva de asientos específicos |

#### 📁 `repository/` - La Alacena

Los repositories son los que van a la base de datos a buscar o guardar información. Es como ir a la alacena a buscar ingredientes.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `EventoRepository.java` | Busca eventos en la base de datos |
| `EstadioRepository.java` | Busca estadios |
| `ZonaRepository.java` | Busca zonas |
| `EntradaRepository.java` | Busca entradas |
| `AsientoEventoRepository.java` | Busca asientos y su estado (disponible, ocupado, reservado) |
| `DisponibilidadZonaRepository.java` | Busca cuántos asientos quedan en cada zona |

#### 📁 `model/` - Los Platos

Los models definen qué es cada cosa. Son como las recetas que dicen qué ingredientes tiene cada plato.

| Archivo | ¿Qué es? |
|---------|---------|
| `Evento.java` | Un evento tiene: nombre, fecha, hora, estadio, tipo (concierto, fútbol, etc.) |
| `Estadio.java` | Un estadio tiene: nombre, ciudad, capacidad |
| `Zona.java` | Una zona tiene: nombre (Platea, Grada), precio, aforo máximo |
| `Entrada.java` | Una entrada tiene: evento, zona, precio, estado (vigente, cancelada, usada) |
| `AsientoEvento.java` | Un asiento tiene: fila, columna, estado (disponible, ocupado, reservado) |
| `DisponibilidadZona.java` | Cuántos asientos quedan disponibles en cada zona de un evento |

#### 📁 `config/` - Las Reglas

| Archivo | ¿Qué hace? |
|---------|-----------|
| `SecurityConfig.java` | Define qué endpoints son públicos y cuáles requieren login |
| `CorsConfig.java` | Permite que el frontend (Angular) hable con el backend |

#### 📁 `filter/` - El Seguridad

| Archivo | ¿Qué hace? |
|---------|-----------|
| `JWTAuthorizationFilter.java` | Revisa cada petición para ver si tenés un token válido |
| `JWTUtil.java` | Crea y verifica los tokens JWT (como un pase de acceso digital) |

---

## Frontend (Angular - TypeScript)

### ¿Qué es el Frontend?

El frontend es lo que ves en tu navegador. Es como la mesa del restaurante: donde sentás, ves el menú, y hacés tu pedido.

### Las carpetas del Frontend

#### 📁 `components/` - Las Pantallas

Cada componente es una pantalla diferente que podés ver.

| Carpeta | ¿Qué ves? |
|---------|-----------|
| `login/` | La pantalla donde ponés tu usuario y contraseña |
| `lista-eventos/` | La pantalla que muestra todos los eventos disponibles |
| `comprar-entrada/` | El formulario donde elegís zona y asiento para comprar |
| `mapa-asientos/` | El mapa visual del estadio donde seleccionás tu asiento (como en el cine) |
| `mis-entradas/` | La pantalla donde ves las entradas que compraste |
| `admin/` | El panel donde el administrador crea eventos, estadios y zonas |

#### 📁 `services/` - Los Mensajeros

| Archivo | ¿Qué hace? |
|---------|-----------|
| `auth.service.ts` | Guarda tu token de login y verifica si estás logueado |
| `boletos.ts` | Hace las peticiones al backend (buscar eventos, comprar entradas, etc.) |

#### 📁 `interceptors/` - El que lleva tu pase

| Archivo | ¿Qué hace? |
|---------|-----------|
| `jwt.interceptor.ts` | Agrega tu token JWT a cada petición automáticamente |

---

## ¿Cómo funciona todo junto?

Imaginate que querés comprar una entrada:

1. **Abrís la página** → `http://localhost:4200/` (Frontend Angular)
2. **Iniciás sesión** → El frontend envía tu usuario y contraseña al backend
3. **El backend verifica** → `AuthController` recibe los datos, `AuthService` los verifica, y te devuelve un token
4. **Ves los eventos** → El frontend pide la lista de eventos al backend (`EventoController` → `EventoService` → `EventoRepository` → Base de datos)
5. **Elegís un evento** → Hacés clic en "Comprar"
6. **Ves el mapa de asientos** → El backend genera los asientos disponibles (`AsientoController` → `AsientoEventoService`)
7. **Seleccionás un asiento** → Hacés clic en un asiento verde (disponible)
8. **Comprás** → El frontend envía tu compra al backend, que verifica que el asiento esté libre y la guarda
9. **Listo** → La entrada aparece en "Mis Entradas"

---

## ¿Cómo arranco el proyecto?

### Opción 1: Desde IntelliJ IDEA

1. Abrí `Main.java` (el archivo principal del backend)
2. Clic derecho → Run 'Main'
3. Abrí una terminal y andá a la carpeta `boletos-frontend/`
4. Ejecutá `npx ng serve`

### Opción 2: Desde la terminal

**Backend:**
```bash
mvn spring-boot:run
```

**Frontend:**
```bash
cd boletos-frontend
npx ng serve
```

### Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | admin123 | Administrador (puede crear eventos) |
| user | user123 | Usuario normal (solo compra entradas) |

---

## Resumen

| Parte | Tecnología | ¿Qué hace? |
|-------|-----------|-----------|
| Backend | Spring Boot (Java) | Procesa todo: login, eventos, compras |
| Frontend | Angular (TypeScript) | Lo que ves en la pantalla |
| Base de datos | H2 (en memoria) | Guarda los datos temporalmente |
| Seguridad | JWT | Tokens de acceso para saber quién sos |
