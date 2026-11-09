# 🎟️ Sistema de Boletos - Instrucciones de Uso

## 📋 Descripción

Sistema de venta de entradas para eventos (conciertos, fútbol, teatro, box) desarrollado con **Spring Boot** (backend) y **Angular** (frontend).

---

## 🚀 Cómo Ejecutar el Proyecto

### 1. Base de Datos MySQL

Asegúrate de tener MySQL instalado y crear una base de datos:

```sql
CREATE DATABASE boletos_db;
```

### 2. Configurar la Base de Datos

En el archivo `src/main/resources/application.properties`, verifica que la conexión esté correcta:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/boletos_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 3. Cargar Datos de Prueba

Ejecuta el script `src/main/resources/data.sql` en tu base de datos MySQL:

**Opción A: Desde MySQL Workbench**
1. Abre MySQL Workbench
2. Conecta a tu base de datos
3. Abre el archivo `data.sql`
4. Ejecuta el script (botón ⚡ o Ctrl+Shift+Enter)

**Opción B: Desde la línea de comandos**
```bash
mysql -u tu_usuario -p boletos_db < src/main/resources/data.sql
```

**Opción C: Desde IntelliJ IDEA**
1. Abre el panel Database (View → Tool Windows → Database)
2. Conecta a tu base de datos MySQL
3. Haz clic derecho en la base de datos → Run SQL File
4. Selecciona `data.sql`

### 4. Ejecutar el Backend (Spring Boot)

```bash
# En el directorio Estadio_proyecto/
mvn spring-boot:run
```

El backend se ejecutará en: `http://localhost:8080`

### 5. Ejecutar el Frontend (Angular)

```bash
# En el directorio Estadio_proyecto/boletos-frontend/
ng serve
```

El frontend se ejecutará en: `http://localhost:4200`

---

## 👥 Usuarios de Prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | Administrador |
| user | user123 | Usuario común |

---

## 📊 Datos de Prueba Cargados

### Estadios
1. **Estadio Nacional** - Lima, Perú (50,000 capacidad)
2. **Estadio Alejandro Villanueva** - Lima, Perú (35,000 capacidad)
3. **Arena Lima** - Lima, Perú (15,000 capacidad)

### Zonas
- **Estadio Nacional**: Platea Preferencia (S/250), Platea General (S/150), Grada Superior (S/80), Grada Inferior (S/120), VIP (S/500)
- **Villanueva**: Platea (S/200), Categoría 1 (S/120), Categoría 2 (S/70), Popular (S/40)
- **Arena Lima**: Gold (S/300), Silver (S/180), Bronce (S/100)

### Eventos
1. **Concierto Coldplay** - 15 Dic 2026, Estadio Nacional
2. **Festival de Rock Lima** - 20 Nov 2026, Estadio Nacional
3. **Concierto Bad Bunny** - 1 Dic 2026, Arena Lima
4. **Alianza Lima vs Universitario** - 10 Nov 2026, Villanueva
5. **Perú vs Brasil** - 18 Nov 2026, Estadio Nacional
6. **David Copperfield** - 5 Dic 2026, Arena Lima
7. **Pelea de Box** - 20 Dic 2026, Villanueva

---

## 🎯 Flujo de Prueba

### Como Usuario Común

1. **Iniciar sesión** con `user/user123`
2. **Ver eventos disponibles** en la página principal
3. **Comprar entrada**:
   - Haz clic en "Comprar Entrada" en cualquier evento
   - Ingresa nombre y DNI
   - Selecciona zona y tipo de entrega
   - Confirma la compra
4. **Ver mis entradas**:
   - Navega a "Mis Entradas"
   - Verás las entradas compradas con su estado (VIGENTE)
5. **Reportar pérdida** (opcional):
   - En "Mis Entradas", haz clic en "Reportar Pérdida"
   - El estado cambiará a PERDIDA
6. **Cancelar entrada** (opcional):
   - En "Mis Entradas", haz clic en "Cancelar"
   - El estado cambiará a CANCELADA y se libera el cupo

### Como Administrador

1. **Iniciar sesión** con `admin/admin123`
2. **Ver eventos** (igual que el usuario)
3. **Comprar entradas** (igual que el usuario)
4. **Gestionar eventos** (vía Postman o API):
   - Crear nuevos eventos: `POST /eventos`
   - Crear estadios: `POST /estadios`
   - Crear zonas: `POST /zonas`

---

## 🔧 Endpoints de la API

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión |
| GET | `/eventos` | Obtener todos los eventos |
| POST | `/eventos` | Crear un nuevo evento (Admin) |
| GET | `/estadios` | Obtener todos los estadios |
| POST | `/estadios` | Crear un nuevo estadio (Admin) |
| GET | `/zonas/{estadioId}` | Obtener zonas de un estadio |
| POST | `/entradas/comprar` | Comprar una entrada |
| GET | `/entradas` | Obtener mis entradas |
| POST | `/entradas/{id}/reportar-perdida` | Reportar entrada perdida |
| POST | `/entradas/{id}/cancelar` | Cancelar entrada |

---

## 🧪 Tests

Para ejecutar los tests unitarios:

```bash
mvn test
```

Tests disponibles:
- `EntradaServiceTest`: 14 tests cubriendo las reglas de negocio del servicio de entradas

---

## 📝 Notas

- El sistema envía correos de confirmación al comprar una entrada. Asegúrate de configurar las credenciales de email en `application.properties`.
- Los eventos tienen fechas futuras para que puedas probar la compra de entradas sin problemas de fecha.
- La seguridad JWT está configurada para proteger los endpoints. El frontend maneja automáticamente el token.
