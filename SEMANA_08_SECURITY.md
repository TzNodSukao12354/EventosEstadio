# Semana 8: Spring Security

## ¿Qué aprendimos esta semana?

Esta semana aprendimos cómo proteger nuestra aplicación con Spring Security. Sin seguridad, cualquiera podría acceder a cualquier endpoint. Con seguridad, solo los usuarios logueados pueden hacer cosas.

## ¿Qué es Spring Security?

Spring Security es como el guardia de seguridad de un edificio. Revisa quién sos antes de dejarte entrar.

Sin Spring Security:
```
Cualquiera → Accede a /eventos, /entradas, /admin
```

Con Spring Security:
```
Usuario → ¿Tenés token? → Sí → ¿Tenés permiso? → Sí → Acceso concedido
                              → No → Acceso denegado (401)
```

## ¿Qué hicimos?

### 1. Configuramos SecurityConfig

El archivo `SecurityConfig.java` define las reglas de seguridad:

| Regla | ¿Qué hace? |
|-------|-----------|
| `csrf().disable()` | Desactiva CSRF (no lo necesitamos en API REST con JWT) |
| `sessionManagement().sessionCreationPolicy.STATELESS` | No creamos sesiones, usamos JWT |
| `authorizeHttpRequests()` | Define qué endpoints son públicos y cuáles requieren login |

### 2. Definimos qué es público y qué es privado

| Endpoint | ¿Quién puede acceder? |
|----------|---------------------|
| `/auth/login` | Cualquiera (necesitás poder loguearte) |
| `/eventos` | Cualquiera (podes ver los eventos sin login) |
| `/estadios` | Cualquiera |
| `/zonas` | Cualquiera |
| `/api/asientos/**` | Cualquiera |
| `/entradas/**` | Solo usuarios logueados |
| `/admin/**` | Solo usuarios con rol ADMIN |

### 3. Creamos usuarios en memoria

Por ahora, los usuarios están definidos en el código (no en la base de datos):

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | admin123 | ADMIN (puede hacer todo) |
| user | user123 | USER (solo compra entradas) |

## ¿Dónde está el código de esta semana?

- `src/main/java/com/example/demo/config/SecurityConfig.java` → Las reglas de seguridad

## Frase clave para defender

> "Configuramos Spring Security para proteger nuestros endpoints, definiendo qué rutas son públicas y cuáles requieren autenticación, usando una arquitectura sin estado (stateless) preparada para JWT."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| Spring Security | El guardia de seguridad de tu aplicación |
| CSRF | Un ataque que Spring protege (lo desactivamos porque usamos JWT) |
| Stateless | Sin sesión, cada petición es independiente |
| authorizeHttpRequests | Define qué endpoints son públicos y cuáles requieren login |
| 401 Unauthorized | No tenés permiso para acceder |
| 403 Forbidden | Tenés login pero no tenés el rol necesario |
