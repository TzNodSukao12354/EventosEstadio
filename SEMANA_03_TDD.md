# Semana 3: TDD (Test-Driven Development)

## ¿Qué aprendimos esta semana?

Esta semana aprendimos qué es TDD y por qué es importante escribir pruebas antes de escribir el código.

## ¿Qué es TDD?

TDD significa "Desarrollo Guiado por Pruebas". Es como cuando hacés un experimento en el colegio: primero decís qué esperás que pase, luego lo hacés, y verificás si salió como esperabas.

TDD tiene 3 pasos:

| Paso | Nombre | ¿Qué hacés? |
|------|--------|-------------|
| 1 | 🔴 Red (Rojo) | Escribís la prueba primero. Obviamente falla porque no hay código aún |
| 2 | 🟢 Green (Verde) | Escribís el código mínimo para que la prueba pase |
| 3 | 🔵 Refactor | Mejorás el código sin romper la prueba |

## ¿Por qué es útil?

Imaginate que escribís código sin pruebas. ¿Cómo sabés que funciona? ¿Lo probás a mano cada vez? Con TDD, las pruebas son automáticas. Si rompes algo, la prueba te avisa.

## ¿Qué hicimos?

Creamos 4 archivos de prueba que verifican que nuestra lógica de negocio funcione correctamente.

### `EntradaServiceTest.java` (14 pruebas)

Este archivo prueba todas las reglas de compra de entradas:

| Prueba | ¿Qué verifica? |
|--------|---------------|
| Comprar entrada exitosa | Que se pueda comprar si el evento es futuro y hay aforo |
| Rechazar si el evento no existe | Que no se pueda comprar si el evento no está en la base de datos |
| Rechazar si el evento ya pasó | Que no se pueda comprar si la fecha del evento ya pasó |
| Rechazar si la zona está llena | Que no se pueda comprar si no quedan asientos |
| Reportar pérdida exitoso | Que se pueda reportar una entrada como perdida |
| Reportar pérdida — entrada no existe | Que no se pueda reportar si la entrada no existe |
| Reimpresión exitosa | Que se pueda reimprimir una entrada perdida |
| Rechazar reimpresión si está PERDIDA | Que no se pueda reimprimir si ya está perdida |
| Marcar usada exitoso | Que se pueda marcar una entrada como usada al entrar |
| Rechazar marcar usada si está PERDIDA | Que no se pueda usar una entrada perdida |
| Rechazar marcar usada si está CANCELADA | Que no se pueda usar una entrada cancelada |
| Cancelar entrada y liberar asiento | Que al cancelar se libere el asiento |
| Rechazar cancelar si ya está CANCELADA | Que no se pueda cancelar dos veces |
| Rechazar cancelar si ya fue USADA | Que no se pueda cancelar si ya se usó |

### `EventoServiceTest.java`

Prueba las operaciones de eventos:
- Crear un evento
- Buscar un evento por ID
- Actualizar un evento
- Borrar un evento
- Buscar eventos por fase

### `EstadioServiceTest.java`

Prueba las operaciones de estadios:
- Crear un estadio
- Buscar un estadio por ID
- Actualizar un estadio
- Borrar un estadio

### `ZonaServiceTest.java`

Prueba las operaciones de zonas:
- Crear una zona
- Buscar una zona por ID
- Verificar disponibilidad de asientos

## ¿Cómo se ven las pruebas?

```java
@Test
@DisplayName("Comprar entrada exitosa si evento futuro y hay aforo")
void comprarEntradaExitosa() {
    // Arrange (Preparar)
    Evento evento = new Evento("Concierto", "2026-12-01", "20:00");
    when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
    
    // Act (Actuar)
    Entrada entrada = entradaService.comprarEntrada(1L, 1L, "12345678");
    
    // Assert (Verificar)
    assertNotNull(entrada);
    assertEquals("VIGENTE", entrada.getEstado());
}
```

## ¿Dónde está el código de esta semana?

- `src/test/java/com/example/demo/service/EntradaServiceTest.java`
- `src/test/java/com/example/demo/service/EventoServiceTest.java`
- `src/test/java/com/example/demo/service/EstadioServiceTest.java`
- `src/test/java/com/example/demo/service/ZonaServiceTest.java`

## ¿Cómo ejecuto las pruebas?

```bash
mvn test
```

## Frase clave para defender

> "Aplicamos principios de TDD para asegurar que las reglas de negocio, como la validación de aforo y fechas, sean robustas y estén libres de errores antes del despliegue."

## Resumen

| Concepto | Explicación simple |
|----------|-------------------|
| TDD | Escribir pruebas antes que código |
| Test unitario | Una prueba que verifica una pequeña parte del código |
| Arrange-Act-Assert | Los 3 pasos de una prueba: preparar, actuar, verificar |
| Mockito | Herramienta para simular objetos en las pruebas |
| @Test | Anotación que dice "esto es una prueba" |
| @DisplayName | Nombre legible para la prueba |
| assert | Verifica que el resultado sea el esperado |
