# Semana 9: Autenticación con JWT

## ¿Qué aprendimos esta semana?

Esta semana aprendimos qué es JWT y cómo usarlo para autenticar a los usuarios sin guardar sesiones en el servidor.

## ¿Qué es JWT?

JWT significa "JSON Web Token". Es como un pase de acceso digital. Cuando iniciás sesión, el servidor te da un token. Cada vez que hacés una petición, mostrás ese token y el servidor verifica que sos vos.

```
Sin JWT:
Usuario → Inicia sesión → Servidor guarda sesión → Servidor devuelve cookie
Usuario → Pide datos → Envía cookie → Servidor busca sesión → Responde

Con JWT:
Usuario → Inicia sesión → Servidor devuelve token
Usuario → Pide datos → Envía token → Servidor verifica token → Responde
```

La diferencia: con JWT, el servidor **no guarda nada**. El token tiene toda la información necesaria.

## ¿Qué hay dentro de un token?

Un token JWT tiene 3 partes:

| Parte | ¿Qué contiene? |
|-------|---------------|
| **Header** | El tipo de token (JWT) y el algoritmo de firma (HS256) |
| **Payload** | Los datos del usuario (usuario, rol, fecha de expiración) |
| **Signature** | Una firma que verifica que el token no fue modificado |

Ejemplo de payload:
```json
{
  "sub": "admin",
  "role": "ADMIN",
  "exp": 1700000000
}
```

## ¿Qué hicimos?

### 1. Creamos `JWTUtil.java`

Este archivo tiene dos funciones principales:

| Método | ¿Qué hace? |
|--------|-----------|
| `generateToken(username)` | Crea un token con el nombre de usuario y el rol |
| `validateToken(token)` | Verifica que el token sea válido y no haya expirado |
| `getUsernameFromToken(token)` | Extrae el nombre de usuario del token |

### 2. Creamos `JWTAuthorizationFilter.java`

Este filtro se ejecuta **antes** de cada petición. Es como el guardia que revisa tu pase antes de dejarte entrar.

```
Petición → JWTAuthorizationFilter → ¿Tenés token? → Sí → ¿Es válido? → Sí → Paso libre
                                                          → No → 401 Unauthorized
                          → No → ¿Es endpoint público? → Sí → Paso libre
                                                        → No → 401 Unauthorized
```

### 3. Creamos `AuthController.java`

El endpoint de login:

```java
@PostMapping("/auth/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // 1. Verificar usuario y contraseña
    // 2. Si son correctos, generar token JWT
    // 3. Devolver el token al frontend
}
```

### 4. Configuramos el interceptor en Angular

En el frontend, creamos `jwt.interceptor.ts` que agrega el token automáticamente a cada petición:

```typescript
intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  const token = this.authService.getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next.handle(req);
}
```

## ¿Dónde está el código de esta semana?

- `src/main/java/com/example/demo/filter/JWTUtil.java` → Crea y verifica tokens
- `src/main/java/com/example/demo/filter/JWTAuthorizationFilter.java` → Revisa cada petición
- `src/main/java/com/example/demo/controller/AuthController.java` → Endpoint de login
- `boletos-frontend/src/app/interceptors/jwt.interceptor.ts` → Agrega token en Angular
- `boletos-frontend/src/app/services/auth.service.ts` → Guarda y maneja el token

## Flujo completo de login

```
1. Usuario pone usuario y contraseña en el login
2. Frontend envía POST /auth/login con los datos
3. AuthController verifica las credenciales
4. Si son correctas, JWTUtil genera un token
5. El token se devuelve al frontend
6. El frontend guarda el token en localStorage
7. Cada petición posterior incluye el token en el header "Authorization: Bearer <token>"
8. JWTAuthorizationFilter verifica el token en cada petición
9. Si el token es válido, la petición continúa
10. Si el token es inválido o expirado, se devuelve 401
```

## Frase clave para defender

> "Implementamos autenticación JWT para que nuestra API sea sin estado (stateless). Al iniciar sesión, el servidor genera un token firmado que el cliente incluye en cada petición, eliminando la necesidad de sesiones en el servidor."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| JWT | Un pase de acceso digital |
| Token | El pase que te dan cuando iniciás sesión |
| Payload | Los datos que lleva el token (usuario, rol) |
| Signature | La firma que verifica que el token no fue modificado |
| Expiración | El tiempo que dura válido el token |
| Bearer | La palabra que va antes del token en el header |
| Interceptor | En Angular, agrega el token automáticamente a cada petición |
| Filter | En Spring, revisa cada petición antes de llegar al controller |
