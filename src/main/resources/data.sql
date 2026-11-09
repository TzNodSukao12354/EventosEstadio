-- ============================================================
-- Script de datos de prueba para el sistema de boletos
-- Se ejecuta automáticamente al iniciar la aplicación (H2)
-- ============================================================

-- ============================================================
-- 1. ESTADIOS
-- ============================================================

INSERT INTO estadios (id, nombre, ciudad, pais, capacidad, hora_apertura_puertas) VALUES
(1, 'Estadio Nacional', 'Lima', 'Perú', 50000, 2),
(2, 'Estadio Alejandro Villanueva', 'Lima', 'Perú', 35000, 2),
(3, 'Arena Lima', 'Lima', 'Perú', 15000, 1);

-- ============================================================
-- 2. ZONAS (por estadio)
-- ============================================================

-- Estadio Nacional (id=1) - Formación ESTADIO (fútbol)
INSERT INTO zonas (id, estadio_id, nombre, precio, aforo_maximo, aforo_actual, formacion) VALUES
(1, 1, 'Platea Preferencia', 250.00, 5000, 0, 'ESTADIO'),
(2, 1, 'Platea General', 150.00, 10000, 0, 'ESTADIO'),
(3, 1, 'Grada Superior', 80.00, 15000, 0, 'ESTADIO'),
(4, 1, 'Grada Inferior', 120.00, 12000, 0, 'ESTADIO'),
(5, 1, 'VIP', 500.00, 2000, 0, 'ESTADIO');

-- Estadio Alejandro Villanueva (id=2) - Formación ESTADIO (fútbol)
INSERT INTO zonas (id, estadio_id, nombre, precio, aforo_maximo, aforo_actual, formacion) VALUES
(6, 2, 'Platea', 200.00, 4000, 0, 'ESTADIO'),
(7, 2, 'Categoría 1', 120.00, 8000, 0, 'ESTADIO'),
(8, 2, 'Categoría 2', 70.00, 12000, 0, 'ESTADIO'),
(9, 2, 'Popular', 40.00, 10000, 0, 'ESTADIO');

-- Arena Lima (id=3) - Formación ARENA (pista en frente)
INSERT INTO zonas (id, estadio_id, nombre, precio, aforo_maximo, aforo_actual, formacion) VALUES
(10, 3, 'Gold', 300.00, 3000, 0, 'ARENA'),
(11, 3, 'Silver', 180.00, 5000, 0, 'ARENA'),
(12, 3, 'Bronce', 100.00, 7000, 0, 'ARENA');

-- Arena Circular (id=4) - Para BTS - Formación CIRCULAR (pista en el centro)
INSERT INTO estadios (id, nombre, ciudad, pais, capacidad, hora_apertura_puertas) VALUES
(4, 'Jockey Plaza Park', 'Lima', 'Perú', 20000, 2);

INSERT INTO zonas (id, estadio_id, nombre, precio, aforo_maximo, aforo_actual, formacion) VALUES
(13, 4, 'Pista Central', 400.00, 3000, 0, 'CIRCULAR'),
(14, 4, 'Grada Frontal', 250.00, 5000, 0, 'CIRCULAR'),
(15, 4, 'Grada Lateral', 180.00, 6000, 0, 'CIRCULAR'),
(16, 4, 'Grada Superior', 100.00, 6000, 0, 'CIRCULAR');

-- ============================================================
-- 3. EVENTOS (futuros, para que puedas comprar entradas)
-- ============================================================

INSERT INTO eventos (id, nombre_evento, tipo_evento, equipo_local, equipo_visitante, artista_principal, estadio_id, fecha, hora, fase) VALUES
-- PRUEBA 1: Estadio de fútbol - Formación ESTADIO
(4, 'Alianza Lima vs Universitario - Final', 'fútbol', 'Alianza Lima', 'Universitario', NULL, 2, '2026-11-10', '15:30:00', 'final'),
(5, 'Perú vs Brasil - Eliminatorias', 'fútbol', 'Perú', 'Brasil', NULL, 1, '2026-11-18', '20:00:00', 'eliminatorias'),

-- PRUEBA 2: BTS - Formación CIRCULAR (pista en el centro, como la imagen)
(8, 'BTS - World Tour Lima 2026', 'concierto', NULL, NULL, 'BTS', 4, '2026-12-20', '19:00:00', NULL),

-- PRUEBA 3: Bad Bunny - Formación ARENA (pista en frente del escenario)
(3, 'Concierto Bad Bunny - Most Wanted Tour', 'concierto', NULL, NULL, 'Bad Bunny', 3, '2026-12-01', '21:00:00', NULL),

-- Otros eventos
(1, 'Concierto Coldplay - World Tour 2026', 'concierto', NULL, NULL, 'Coldplay', 1, '2026-12-15', '20:00:00', NULL),
(2, 'Festival de Rock Lima 2026', 'concierto', NULL, NULL, 'Varios artistas', 1, '2026-11-20', '18:00:00', NULL),
(6, 'Espectáculo de Magia - David Copperfield', 'teatro', NULL, NULL, 'David Copperfield', 3, '2026-12-05', '19:00:00', NULL),
(7, 'Pelea de Box - Título Mundial', 'box', NULL, NULL, 'Campeón mundial', 2, '2026-12-20', '22:00:00', NULL);

-- ============================================================
-- 4. DISPONIBILIDAD ZONA (relación evento-zona)
-- ============================================================

-- Evento 1: Coldplay (Estadio Nacional) - zonas 1-5
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(1, 1, 0), (1, 2, 0), (1, 3, 0), (1, 4, 0), (1, 5, 0);

-- Evento 2: Festival Rock (Estadio Nacional) - zonas 1-5
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(2, 1, 0), (2, 2, 0), (2, 3, 0), (2, 4, 0), (2, 5, 0);

-- Evento 3: Bad Bunny (Arena Lima) - ARENA - zonas 10-12
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(3, 10, 0), (3, 11, 0), (3, 12, 0);

-- Evento 4: Alianza vs Universitario (Villanueva) - ESTADIO - zonas 6-9
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(4, 6, 0), (4, 7, 0), (4, 8, 0), (4, 9, 0);

-- Evento 5: Perú vs Brasil (Estadio Nacional) - ESTADIO - zonas 1-5
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(5, 1, 0), (5, 2, 0), (5, 3, 0), (5, 4, 0), (5, 5, 0);

-- Evento 6: David Copperfield (Arena Lima) - ARENA - zonas 10-12
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(6, 10, 0), (6, 11, 0), (6, 12, 0);

-- Evento 7: Box (Villanueva) - ESTADIO - zonas 6-9
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(7, 6, 0), (7, 7, 0), (7, 8, 0), (7, 9, 0);

-- Evento 8: BTS (Jockey Plaza Park) - CIRCULAR - zonas 13-16
INSERT INTO disponibilidad_zona (evento_id, zona_id, aforo_ocupado) VALUES
(8, 13, 0), (8, 14, 0), (8, 15, 0), (8, 16, 0);
