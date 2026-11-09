# Guía para probar el backend en Postman

## Antes de empezar
1. Descomprime el zip.
2. Abre la carpeta `backend` como proyecto Maven en **Spring Tool Suite / Eclipse / IntelliJ**
   (igual que hiciste en clase con `proyecto006`).
3. Corre `DemoApplication.java` (botón derecho -> Run As -> Spring Boot App).
4. Espera a que la consola diga que el servidor arrancó en el puerto **8080**.
5. Abre Postman. Todas las URL empiezan con: `http://localhost:8080`

---

## 1. Estadios

**Ver todos los estadios**
`GET http://localhost:8080/estadios`

**Ver un estadio por id**
`GET http://localhost:8080/estadios/1`

**Crear un estadio nuevo**
`POST http://localhost:8080/estadios`
Body -> raw -> JSON:
```json
{
  "id": 4,
  "nombre": "Estadio Nacional",
  "ciudad": "Lima",
  "pais": "Peru",
  "capacidad": 40000,
  "horaAperturaPuertas": 2
}
```

---

## 2. Zonas (tribunas)

**Ver todas las zonas**
`GET http://localhost:8080/zonas`

**Ver las zonas de un estadio específico** (esta es la que sale en el PPT)
`GET http://localhost:8080/estadios/1/zonas`

**Crear una zona nueva**
`POST http://localhost:8080/zonas`
Body -> raw -> JSON:
```json
{
  "id": 6,
  "estadioId": 1,
  "nombre": "Tribuna Familiar",
  "precio": 60.0,
  "aforoMaximo": 3000,
  "aforoActual": 0
}
```

---

## 3. Eventos (partidos)

**Ver todos los eventos**
`GET http://localhost:8080/eventos`

**Filtrar eventos por fase** (ej: solo la final)
`GET http://localhost:8080/eventos?fase=final`

**Ver un evento por id**
`GET http://localhost:8080/eventos/1`

---

## 4. Entradas (lo más importante: aquí están las reglas de negocio)

### Comprar una entrada (valida que la zona tenga aforo libre)
`POST http://localhost:8080/entradas`
Body -> raw -> JSON:
```json
{
  "id": 1,
  "eventoId": 1,
  "zonaId": 1,
  "nombreUsuario": "Juan Perez",
  "documentoUsuario": "12345678",
  "estado": "VIGENTE",
  "tipoEntrega": "E-TICKET"
}
```
Respuesta esperada: `OK: entrada comprada correctamente`

Si repites esta compra muchas veces hasta llenar el `aforoMaximo` de esa zona (15000 en
Popular Norte), la siguiente compra debe responder:
`ERROR: la zona ya no tiene aforo disponible`

### Ver una entrada
`GET http://localhost:8080/entradas/1`

### Reportar una entrada como perdida
`PUT http://localhost:8080/entradas/1/reportar-perdida`
Respuesta esperada: `La entrada fue marcada como PERDIDA. No se repone ni se reembolsa...`

### Pedir reimpresión (ya no funciona si la entrada está PERDIDA)
`POST http://localhost:8080/entradas/1/reimpresion`
- Si la entrada está VIGENTE: `Reimpresion generada. Costo adicional: S/10`
- Si la entrada está PERDIDA: `ERROR: una entrada PERDIDA no se puede reimprimir`

### Validar si todavía se puede ingresar (según la hora)
`GET http://localhost:8080/entradas/1/validar-ingreso`

Esto compara la hora actual contra la hora del evento menos las horas de apertura de
puertas del estadio (dato investigado: 2 a 4 horas antes). Como el evento 1 tiene fecha
2026-09-20, hoy todavía va a responder: `Todavia no abren las puertas. Apertura: ...`

### Marcar una entrada como usada (cuando la persona ya entró al estadio)
`PUT http://localhost:8080/entradas/1/marcar-usada`
Respuesta esperada: `Entrada marcada como USADA. La persona ya ingreso al estadio.`

### "Eliminar" una entrada (en realidad la cancela, no la borra)
`DELETE http://localhost:8080/entradas/1`
Respuesta esperada: `Entrada CANCELADA. El cupo de la zona fue liberado para otra persona.`

No borra el registro de la lista a propósito — solo cambia su estado a `CANCELADA` y
libera el aforo que esa entrada ocupaba, para que otra persona pueda comprar en esa zona.

---

## Notas
- Todo se guarda en memoria (ArrayList), igual que el ejemplo de "Chiste" del curso.
  Si reinicias la aplicación, los datos vuelven a los valores iniciales.
- Los datos de ejemplo (estadios, zonas, eventos) ya vienen cargados en cada Service,
  en su constructor — no necesitas crearlos a mano para empezar a probar.
