# Referencias de Diseño — Sistemas de Boletos con Selección de Asientos

Este archivo recopila links de proyectos similares al tuyo para que tengas inspiración visual y funcional. Organizalo por categoría para que encuentres rápido lo que necesitás.

---

## 🎯 Librerías Open Source (para usar en tu proyecto)

### 1. Seatmap Canvas — Recomendada ⭐
- **GitHub:** https://github.com/alisaitteke/seatmap-canvas
- **Demo en vivo:** https://alisaitteke.github.io/seatmap-canvas/
- **Qué es:** Una librería open source en TypeScript para crear mapas de asientos interactivos. Funciona con React, Vue, Angular, y vanilla JS.
- **Por qué te sirve:** Podés integrarla directamente en tu frontend de Angular para reemplazar el mapa actual por uno más profesional.
- **Características:**
  - Selección dinámica de asientos
  - Colores personalizables (disponible, ocupado, seleccionado)
  - Imágenes de fondo por zona
  - Zoom y navegación
  - Leyenda automática

### 2. alf.io — Sistema completo de boletos
- **GitHub:** https://github.com/alfio-event/alf.io
- **Sitio web:** https://alf.io/
- **Qué es:** Un sistema completo de reserva de entradas open source hecho en Java (como tu backend).
- **Por qué te sirve:** Podés ver cómo diseñaron el flujo completo: desde la lista de eventos hasta la selección de asientos y el pago.
- **Características:**
  - Mapa de asientos interactivo
  - Gestión de eventos y zonas
  - Check-in y escaneo de entradas
  - Hecho en Java/Spring (similar a tu stack)

---

## 🖥️ Demos en Vivo (para ver y probar)

### 1. SeatMap Pro
- **Sitio web:** https://seatmap.pro/
- **Qué es:** Una herramienta profesional para crear mapas de asientos interactivos.
- **Por qué te sirve:** Podés ver cómo se ve un mapa profesional con zoom, leyenda, y selección de asientos. No es open source, pero te da una idea de cómo podría verse tu sistema.

### 2. ByTable / TouchOffice
- **Video demo:** https://www.youtube.com/watch?v=XJuixSRskT0
- **Qué es:** Un sistema de ticketing con selección de asientos.
- **Por qué te sirve:** El video muestra el flujo completo: desde crear el plano del estadio hasta que el usuario selecciona su asiento.

### 3. Event Seat Reservation System
- **Video demo:** https://www.youtube.com/watch?v=-N2aUvVytmw
- **Qué es:** Demo de un sistema de reserva de asientos con diferentes zonas (VIP, Standard, Economy).
- **Por qué te sirve:** Mostrá cómo se ven los asientos disponibles vs ocupados, cómo se seleccionan, y cómo se genera el ticket final.

---

## 🎨 Plataformas Comerciales (para inspiración visual)

### 1. Ticketmaster
- **Sitio web:** https://www.ticketmaster.com/
- **Qué ver:** Andá a cualquier evento y hacé clic en "Comprar entradas". Verás el mapa de asientos con:
  - Zonas coloreadas por precio
  - Asientos disponibles en verde, ocupados en gris
  - Zoom para acercar/alejar
  - Vista desde el asiento seleccionado

### 2. Entradas.com (Latinoamérica)
- **Sitio web:** https://www.entradas.com/
- **Qué ver:** Similar a Ticketmaster, pero más usado en Argentina, México, Colombia, etc. El mapa de asientos muestra:
  - Planos reales de estadios
  - Precios por zona
  - Asientos disponibles y ocupados

### 3. Vivenu (tecnología de mapas de asientos)
- **Artículo:** https://vivenu.com/blog/best-ticketing-system-seat-map-accuracy
- **Qué ver:** Este artículo explica cómo los grandes estadios diseñan sus mapas de asientos. Incluye ejemplos reales:
  - **FC Schalke 04:** 65,000 entradas vendidas en menos de 3 minutos
  - **Starlite Occident:** Vista 360° desde cada asiento
  - **SuperTicket (Ecuador):** Soporta 6 equipos de fútbol con mapas personalizados

---

## 📚 Artículos de Diseño (para entender la arquitectura)

### 1. System Design: Ticketmaster
- **Artículo:** https://www.hellointerview.com/learn/system-design/problem-breakdowns/ticketmaster
- **Qué aprendés:** Cómo diseñar un sistema como Ticketmaster desde cero. Explica:
  - Cómo se guarda el mapa de asientos (JSON)
  - Cómo se bloquea un asiento cuando un usuario lo selecciona (10 minutos de timeout)
  - Cómo se evita que dos usuarios compren el mismo asiento

### 2. UX Design: Concert Tickets
- **Artículo:** https://medium.com/did-you-know-the-journal-blog/ux-design-project-concert-tickets-a16c0dcd332e
- **Qué aprendés:** Cómo diseñar la experiencia de usuario para comprar entradas de conciertos. Incluye:
  - Flujo de compra paso a paso
  - Diseño de la pantalla de selección de asientos
  - Cómo mostrar el ticket final

### 3. Reddit — UX Design Inspiration
- **Hilo:** https://www.reddit.com/r/UXDesign/comments/1mwo21y/looking_for_ticket_ui_inspiration
- **Qué aprendés:** Discusión sobre diseño de tickets y UI. Útil para ideas de cómo mostrar las entradas compradas.

---

## 🎬 Videos Tutoriales

### 1. Cómo diseñar un plano de asientos
- **Video:** https://www.youtube.com/watch?v=XJuixSRskT0
- **Duración:** 6 minutos
- **Qué mostrá:** Paso a paso cómo crear un plano de asientos, configurar zonas, y habilitar la venta online.

### 2. SeatMap Pro — Integración completa
- **Video:** https://www.youtube.com/watch?v=128_Sp0WYNk
- **Duración:** 4 minutos
- **Qué mostrá:** Cómo integrar un mapa de asientos interactivo en una web de ticketing.

### 3. Sistema de reserva de asientos completo
- **Video:** https://www.youtube.com/watch?v=-N2aUvVytmw
- **Duración:** 5 minutos
- **Qué mostrá:** Demo completa de un sistema con backend y frontend, incluyendo selección de asientos, pago, y generación de tickets.

---

## 💡 Ideas para tu proyecto

Basado en estas referencias, acá tenés ideas que podés implementar:

### Mejoras visuales
1. **Zoom en el mapa** — Permitir acercar/alejar el plano del estadio
2. **Vista desde el asiento** — Mostrar una foto o video desde el asiento seleccionado (como Starlite Occident)
3. **Colores por precio** — Cada zona tiene un color diferente según el precio
4. **Leyenda clara** — Siempre visible: verde = disponible, rojo = ocupado, azul = seleccionado

### Mejoras funcionales
1. **Timer de reserva** — Cuando un usuario selecciona un asiento, lo bloqueás por 10 minutos (como Ticketmaster)
2. **Búsqueda de asientos** — Permitir buscar por fila y columna específica
3. **Asientos juntos** — Sugerir asientos disponibles cerca de un asiento seleccionado
4. **Vista 3D** — Mostrar una vista 3D del estadio (más avanzado)

### Mejoras de UX
1. **Confirmación visual** — Antes de comprar, mostrar un resumen con el asiento seleccionado, la zona, y el precio
2. **Ticket digital** — Generar un QR que el usuario pueda guardar en su celular
3. **Historial de compras** — Mostrar las entradas compradas con el número de asiento

---

## 📋 Resumen de Links

| Tipo | Link | Qué te da |
|------|------|-----------|
| **Librería open source** | https://alisaitteke.github.io/seatmap-canvas/ | Mapa de asientos listo para usar |
| **Sistema completo Java** | https://alf.io/ | Ejemplo completo en Java/Spring |
| **Demo profesional** | https://seatmap.pro/ | Inspiración visual profesional |
| **Ticketmaster** | https://www.ticketmaster.com/ | Referencia comercial principal |
| **Entradas.com** | https://www.entradas.com/ | Referencia latinoamericana |
| **Artículo de arquitectura** | https://www.hellointerview.com/learn/system-design/problem-breakdowns/ticketmaster | Cómo diseñar el sistema |
| **Video: plano de asientos** | https://www.youtube.com/watch?v=XJuixSRskT0 | Tutorial paso a paso |
| **Video: sistema completo** | https://www.youtube.com/watch?v=-N2aUvVytmw | Demo de reserva con backend |

---

## 🚀 ¿Qué hacer ahora?

1. **Abrí los links** en orden y mirá cómo se ven los mapas de asientos
2. **Probá la demo de seatmap-canvas** para ver si te gusta el estilo
3. **Mirá los videos** para entender el flujo completo
4. **Elegí qué ideas** querés implementar en tu proyecto
5. **Decime qué te gustó** y te ayudo a implementarlo
