# Semanas 13-14: Servicios y HTTP en Angular

## ¿Qué aprendimos estas semanas?

Estas semanas aprendimos cómo hacer que el frontend (Angular) hable con el backend (Spring Boot) usando servicios y peticiones HTTP.

## ¿Qué es un Servicio en Angular?

Un servicio es como un mensajero. En vez de que cada componente haga peticiones al backend por su cuenta, creamos un servicio central que se encarga de todo.

```
Sin servicio:
Componente Login → Pide al backend
Componente Eventos → Pide al backend
Componente Entradas → Pide al backend
(Cada uno repite código)

Con servicio:
Componente Login → Servicio → Backend
Componente Eventos → Servicio → Backend
Componente Entradas → Servicio → Backend
(El servicio centraliza todo)
```

## ¿Qué hicimos?

### 1. Creamos `boletos.ts` (BoletosService)

Este servicio hace todas las peticiones al backend:

| Método | Petición HTTP | ¿Qué hace? |
|--------|--------------|-----------|
| `getEventos()` | GET /eventos | Obtiene la lista de eventos |
| `getEstadios()` | GET /estadios | Obtiene la lista de estadios |
| `getZonas()` | GET /zonas | Obtiene la lista de zonas |
| `comprarEntrada(datos)` | POST /entradas | Compra una entrada |
| `getMisEntradas()` | GET /entradas | Obtiene mis entradas |
| `reportarPerdida(id)` | PUT /entradas/{id}/reportar-perdida | Reporta una entrada perdida |
| `cancelarEntrada(id)` | DELETE /entradas/{id} | Cancela una entrada |
| `comprarEntradaConAsiento(datos)` | POST /entradas + POST /api/asientos/reservar | Compra con asiento específico |

Ejemplo de código:
```typescript
getEventos(): Observable<Evento[]> {
  return this.http.get<Evento[]>('http://localhost:8080/eventos');
}
```

### 2. Creamos `auth.service.ts` (AuthService)

Este servicio maneja la autenticación:

| Método | ¿Qué hace? |
|--------|-----------|
| `login(usuario, contraseña)` | Envía las credenciales al backend y guarda el token |
| `logout()` | Borra el token y cierra sesión |
| `getToken()` | Devuelve el token guardado |
| `isAuthenticated()` | Verifica si hay un token válido |
| `isAdmin()` | Verifica si el usuario tiene rol ADMIN |
| `getUsername()` | Devuelve el nombre de usuario |

### 3. Creamos `jwt.interceptor.ts`

El interceptor es como un guardia que revisa cada petición saliente y le agrega el token JWT automáticamente:

```typescript
intercept(req: HttpRequest<any>, next: HttpHandler) {
  const token = this.authService.getToken();
  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }
  return next.handle(req);
}
```

## ¿Cómo funciona una petición completa?

```
1. Componente (ej: lista-eventos) llama a boletosService.getEventos()
2. BoletosService hace la petición HTTP al backend
3. JWT Interceptor intercepta la petición y agrega el token
4. La petición llega al backend con el token en el header
5. JWTAuthorizationFilter verifica el token
6. Si es válido, el controller responde con los datos
7. Los datos vuelven al componente y se muestran en pantalla
```

## ¿Dónde está el código de estas semanas?

- `boletos-frontend/src/app/services/boletos.ts` → Servicio de peticiones HTTP
- `boletos-frontend/src/app/services/auth.service.ts` → Servicio de autenticación
- `boletos-frontend/src/app/interceptors/jwt.interceptor.ts` → Interceptor JWT

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Servicio | Un mensajero central que hace las peticiones al backend |
| HttpClient | Herramienta de Angular para hacer peticiones HTTP |
| Observable | Un valor que puede cambiar con el tiempo (respuesta HTTP) |
| Interceptor | Un guardia que revisa cada petición antes de enviarla |
| @Injectable | Anotación que dice "esta clase es un servicio" |
| localStorage | Memoria del navegador donde guardamos el token |
