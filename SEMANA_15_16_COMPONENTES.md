# Semanas 15-16: Componentes del Frontend

## ¿Qué aprendimos estas semanas?

Estas semanas creamos todas las pantallas (componentes) de nuestra aplicación: login, lista de eventos, compra de entradas, mis entradas, y el panel de admin.

## Los componentes que creamos

### 1. Login (`login/`)

La pantalla donde iniciás sesión.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `login.ts` | Recibe el usuario y contraseña, los envía al backend, y guarda el token |
| `login.html` | Los campos de usuario, contraseña, y el botón "Iniciar Sesión" |
| `login.css` | Los estilos: colores, tamaños, posiciones |

**Flujo:**
```
1. Usuario pone usuario y contraseña
2. Hacé clic en "Iniciar Sesión"
3. login.ts envía los datos al backend (POST /auth/login)
4. Si son correctos, el backend devuelve un token
5. El token se guarda en localStorage
6. Se redirige a la lista de eventos
```

### 2. Lista de Eventos (`lista-eventos/`)

La pantalla principal donde ves todos los eventos disponibles.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `lista-eventos.ts` | Carga la lista de eventos desde el backend |
| `lista-eventos.html` | Muestra cada evento como una tarjeta con nombre, fecha, hora y botón "Comprar" |
| `lista-eventos.css` | Estilos de las tarjetas |

**¿Qué ves?**
- Una barra superior con tu nombre y enlaces a otras pantallas
- Una lista de tarjetas, una por cada evento
- Cada tarjeta muestra: nombre del evento, fecha, hora, tipo
- Un botón "Comprar" que abre el formulario de compra

### 3. Comprar Entrada (`comprar-entrada/`)

El formulario donde elegís zona y asiento para comprar.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `comprar-entrada.ts` | Carga las zonas disponibles, procesa la compra |
| `comprar-entrada.html` | El formulario con selector de zona, mapa de asientos, y tipo de entrega |
| `comprar-entrada.css` | Estilos del formulario |

**Flujo de compra:**
```
1. Elegís un evento en la lista
2. Se abre el formulario de compra
3. Elegís la zona (Platea, Grada, VIP)
4. Ves el mapa de asientos
5. Hacé clic en un asiento verde (disponible)
6. Elegís tipo de entrega (E-Ticket o Física)
7. Hacé clic en "Comprar"
8. La entrada se guarda y aparece en "Mis Entradas"
```

### 4. Mis Entradas (`mis-entradas/`)

La pantalla donde ves las entradas que compraste.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `mis-entradas.ts` | Carga tus entradas desde el backend |
| `mis-entradas.html` | Muestra cada entrada como una tarjeta con información del evento |
| `mis-entradas.css` | Estilos de las tarjetas |

**¿Qué ves?**
- Una tarjeta por cada entrada que compraste
- Cada tarjeta muestra: evento, fecha, estadio, zona, estado
- Botones para: reportar pérdida, cancelar entrada

### 5. Admin (`admin/`)

El panel de administración donde creás eventos, estadios y zonas. Solo accesible para usuarios con rol ADMIN.

| Archivo | ¿Qué hace? |
|---------|-----------|
| `admin.ts` | Carga eventos, estadios y zonas; crea nuevos registros |
| `admin.html` | Pestañas para eventos, estadios y zonas; formularios para crear |
| `admin.css` | Estilos del panel |

**¿Qué podés hacer?**
- Crear nuevos eventos (nombre, fecha, hora, estadio, tipo)
- Crear nuevos estadios (nombre, ciudad, capacidad)
- Crear nuevas zonas (nombre, precio, aforo)
- Ver las listas de eventos, estadios y zonas registrados

## ¿Dónde está el código de estas semanas?

- `boletos-frontend/src/app/components/login/` → Login
- `boletos-frontend/src/app/components/lista-eventos/` → Lista de eventos
- `boletos-frontend/src/app/components/comprar-entrada/` → Comprar entrada
- `boletos-frontend/src/app/components/mis-entradas/` → Mis entradas
- `boletos-frontend/src/app/components/admin/` → Panel de admin

## Resumen de componentes

| Componente | URL | ¿Quién puede acceder? |
|-----------|-----|---------------------|
| Login | `/login` | Cualquiera |
| Lista de eventos | `/eventos` | Usuarios logueados |
| Comprar entrada | Se abre desde lista de eventos | Usuarios logueados |
| Mis entradas | `/mis-entradas` | Usuarios logueados |
| Admin | `/admin` | Solo ADMIN |

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Componente | Una pantalla con lógica, estructura y estilo |
| Template (HTML) | La estructura de lo que ves |
| Styles (CSS) | Los colores, tamaños y posiciones |
| Class (TS) | La lógica: qué pasa cuando hacés clic |
| ngIf | Muestra algo solo si una condición es verdadera |
| ngFor | Repite algo para cada elemento de una lista |
| (click) | Ejecuta código cuando hacés clic |
| [(ngModel)] | Vincula un campo de formulario con una variable |
