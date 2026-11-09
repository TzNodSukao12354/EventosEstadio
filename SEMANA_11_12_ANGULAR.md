# Semanas 11-12: Introducción a Angular

## ¿Qué aprendimos estas semanas?

Estas semanas aprendimos qué es Angular, cómo crear un proyecto, y cómo funcionan los componentes y las rutas.

## ¿Qué es Angular?

Angular es un framework de Google para crear aplicaciones web. Es como el "cuerpo" de tu página web: define qué se ve en cada pantalla y cómo se comporta.

| Parte | Tecnología |
|-------|-----------|
| Backend | Spring Boot (Java) - La cocina |
| Frontend | Angular (TypeScript) - La mesa |

## ¿Qué es TypeScript?

TypeScript es como Java pero para el frontend. Es similar a JavaScript pero con tipos de datos más estrictos, como Java.

```typescript
// TypeScript (con tipos)
let nombre: string = "Juan";
let edad: number = 25;

// JavaScript (sin tipos)
let nombre = "Juan";
let edad = 25;
```

## ¿Qué hicimos?

### 1. Creamos el proyecto con Angular CLI

Angular CLI es como Spring Initializr pero para Angular. Genera el proyecto base con un solo comando:

```bash
ng new boletos-frontend
```

### 2. Estructura del proyecto

```
boletos-frontend/
└── src/app/
    ├── components/      ← Las pantallas (login, eventos, compras)
    ├── services/        ← Los que hablan con el backend
    ├── interceptors/    ← El que lleva tu token JWT
    ├── app.routes.ts    ← Las rutas (qué pantalla se muestra en cada URL)
    └── app.ts           ← El componente principal
```

### 3. ¿Qué es un Componente?

Un componente es como una pantalla. Cada componente tiene 3 archivos:

| Archivo | ¿Qué es? |
|---------|---------|
| `.ts` | La lógica (TypeScript) - El cerebro |
| `.html` | La estructura (HTML) - El esqueleto |
| `.css` | El estilo (CSS) - La ropa |

Ejemplo:
```
login/
├── login.ts      ← Lógica: qué pasa cuando hacés clic en "Iniciar Sesión"
├── login.html    ← Estructura: los campos de usuario y contraseña
└── login.css     ← Estilo: colores, tamaños, posiciones
```

### 4. ¿Qué son las Rutas?

Las rutas definen qué componente se muestra en cada URL.

| URL | Componente |
|-----|-----------|
| `/login` | LoginComponent |
| `/eventos` | ListaEventosComponent |
| `/mis-entradas` | MisEntradasComponent |
| `/admin` | AdminComponent |

Configuración en `app.routes.ts`:
```typescript
const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'eventos', component: ListaEventosComponent },
  { path: 'mis-entradas', component: MisEntradasComponent },
  { path: 'admin', component: AdminComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
```

## ¿Dónde está el código de estas semanas?

- `boletos-frontend/` → Todo el proyecto de Angular
- `boletos-frontend/src/app/app.routes.ts` → Las rutas
- `boletos-frontend/src/app/app.ts` → El componente principal

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Angular | Framework de Google para hacer frontends |
| TypeScript | JavaScript con tipos (como Java pero para web) |
| Componente | Una pantalla con lógica, estructura y estilo |
| Ruta | Define qué pantalla se muestra en cada URL |
| Angular CLI | Herramienta de línea de comandos para Angular |
| .ts | Archivo de TypeScript (lógica) |
| .html | Archivo HTML (estructura) |
| .css | Archivo CSS (estilo) |
