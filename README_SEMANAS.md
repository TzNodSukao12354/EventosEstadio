# 📚 Documentación del Proyecto - Índice

## ¿De qué trata este proyecto?

Es un **sistema de venta de boletos para estadios**. Es como Ticketmaster o Entradas.com, pero lo hicimos nosotros desde cero como proyecto del curso.

---

## 📖 Cómo usar esta documentación

### ¿Querés entender el proyecto en general?
→ Leé primero: **[EXPLICACION_PROYECTO.md](EXPLICACION_PROYECTO.md)**

### ¿Querés saber qué se hizo en cada semana?
→ Usá este índice para navegar por semana.

---

## 📅 Archivo por Semana

### Backend (Spring Boot - Java)

| Semana | Tema | Archivo | ¿Qué aprendés? |
|--------|------|---------|---------------|
| **Semana 1** | Spring Boot y Starters | [SEMANA_01_SPRING_BOOT.md](SEMANA_01_SPRING_BOOT.md) | Cómo crear un proyecto con Spring Initializr |
| **Semana 2** | Endpoints y Controladores | [SEMANA_02_ENDPOINTS.md](SEMANA_02_ENDPOINTS.md) | Cómo crear rutas (GET, POST, PUT, DELETE) |
| **Semana 3** | TDD (Pruebas) | [SEMANA_03_TDD.md](SEMANA_03_TDD.md) | Cómo escribir pruebas unitarias |
| **Semana 4** | API REST | [SEMANA_04_API_REST.md](SEMANA_04_API_REST.md) | Cómo funciona una API RESTful |
| **Semana 6** | Base de Datos (JPA) | [SEMANA_06_BASE_DE_DATOS.md](SEMANA_06_BASE_DE_DATOS.md) | Cómo guardar y buscar datos |
| **Semana 8** | Spring Security | [SEMANA_08_SECURITY.md](SEMANA_08_SECURITY.md) | Cómo proteger los endpoints |
| **Semana 9** | JWT (Tokens) | [SEMANA_09_JWT.md](SEMANA_09_JWT.md) | Cómo funciona el login con tokens |

### Frontend (Angular - TypeScript)

| Semana | Tema | Archivo | ¿Qué aprendés? |
|--------|------|---------|---------------|
| **Semana 11-12** | Angular Intro | [SEMANA_11_12_ANGULAR.md](SEMANA_11_12_ANGULAR.md) | Qué es Angular, componentes y rutas |
| **Semana 13-14** | Servicios y HTTP | [SEMANA_13_14_SERVICIOS_HTTP.md](SEMANA_13_14_SERVICIOS_HTTP.md) | Cómo el frontend habla con el backend |
| **Semana 15-16** | Componentes | [SEMANA_15_16_COMPONENTES.md](SEMANA_15_16_COMPONENTES.md) | Las pantallas: login, eventos, compras, admin |
| **Semana 17** | Mapa de Asientos | [SEMANA_17_MAPA_ASIENTOS.md](SEMANA_17_MAPA_ASIENTOS.md) | El mapa visual tipo cine para elegir asiento |

---

## 🗂️ Resumen de archivos creados

| Archivo | Descripción |
|---------|-------------|
| `EXPLICACION_PROYECTO.md` | Explicación general del proyecto en vocabulario simple |
| `SEMANA_01_SPRING_BOOT.md` | Semana 1: Spring Boot y Starters |
| `SEMANA_02_ENDPOINTS.md` | Semana 2: Endpoints y Controladores |
| `SEMANA_03_TDD.md` | Semana 3: Pruebas unitarias (TDD) |
| `SEMANA_04_API_REST.md` | Semana 4: API REST |
| `SEMANA_06_BASE_DE_DATOS.md` | Semana 6: Base de datos con JPA |
| `SEMANA_08_SECURITY.md` | Semana 8: Spring Security |
| `SEMANA_09_JWT.md` | Semana 9: Autenticación con JWT |
| `SEMANA_11_12_ANGULAR.md` | Semanas 11-12: Introducción a Angular |
| `SEMANA_13_14_SERVICIOS_HTTP.md` | Semanas 13-14: Servicios y HTTP |
| `SEMANA_15_16_COMPONENTES.md` | Semanas 15-16: Componentes del frontend |
| `SEMANA_17_MAPA_ASIENTOS.md` | Semana 17: Mapa de asientos visuales |

---

## 🚀 ¿Cómo arranco el proyecto?

### Backend (Spring Boot)
```bash
mvn spring-boot:run
```

### Frontend (Angular)
```bash
cd boletos-frontend
npx ng serve
```

### Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | admin123 | Administrador |
| user | user123 | Usuario normal |

### URLs

| Servicio | URL |
|----------|-----|
| Frontend | http://localhost:4200/ |
| Backend | http://localhost:8080/ |
