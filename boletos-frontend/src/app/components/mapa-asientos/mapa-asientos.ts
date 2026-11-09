import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, Inject, PLATFORM_ID, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface AsientoFila {
  columna: number;
  estado: string;
  id: number;
}

interface MapaAsientos {
  zonaId: number;
  zonaNombre: string;
  precio: number;
  aforoMaximo: number;
  ocupados: number;
  disponibles: number;
  formacion: string; // ESTADIO, CIRCULAR, ARENA
  filas: { [key: string]: AsientoFila[] };
}

interface AsientoPos {
  x: number;
  y: number;
  fila: string;
  columna: number;
  estado: string;
}

@Component({
  selector: 'app-mapa-asientos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mapa-asientos.html',
  styleUrl: './mapa-asientos.css'
})
export class MapaAsientosComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() eventoId: number = 0;
  @Input() zonaId: number = 0;
  @Output() asientoSeleccionado = new EventEmitter<{ fila: string; columna: number; zonaId: number; precio: number }>();

  @ViewChild('canvasRef') canvasRef!: ElementRef<HTMLCanvasElement>;

  mapa: MapaAsientos | null = null;
  filaSeleccionada: string = '';
  columnaSeleccionada: number = 0;
  cargando: boolean = false;
  mensaje: string = '';
  tipoMensaje: string = '';
  listaFilas: string[] = [];

  // Canvas y zoom
  private canvas: HTMLCanvasElement | null = null;
  private ctx: CanvasRenderingContext2D | null = null;
  private asientosPos: AsientoPos[] = [];

  // Zoom y pan
  private scale = 1;
  private offsetX = 0;
  private offsetY = 0;
  private isDragging = false;
  private dragStartX = 0;
  private dragStartY = 0;
  private dragOffsetX = 0;
  private dragOffsetY = 0;

  // Dimensiones del canvas
  private canvasWidth = 0;
  private canvasHeight = 0;

  // Colores
  private colores = {
    disponible: '#28a745',
    ocupado: '#dc3545',
    seleccionado: '#007bff',
    hover: '#ffc107',
    escenario: '#6c757d',
    fondo: '#f8f9fa',
    texto: '#ffffff',
    grid: '#dee2e6'
  };

  private apiUrl = 'http://localhost:8080/api/asientos';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId) && this.eventoId && this.zonaId) {
      this.cargarMapa();
    }
  }

  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    this.canvas = this.canvasRef.nativeElement;
    this.ctx = this.canvas.getContext('2d');

    // Ajustar tamaño del canvas
    this.adjustCanvasSize();

    // Event listeners para zoom y pan
    this.canvas.addEventListener('wheel', (e: WheelEvent) => this.onWheel(e));
    this.canvas.addEventListener('mousedown', (e: MouseEvent) => this.onMouseDown(e));
    this.canvas.addEventListener('mousemove', (e: MouseEvent) => this.onMouseMove(e));
    this.canvas.addEventListener('mouseup', () => this.onMouseUp());
    this.canvas.addEventListener('mouseleave', () => this.onMouseUp());
    this.canvas.addEventListener('click', (e: MouseEvent) => this.onClick(e));

    window.addEventListener('resize', () => {
      this.adjustCanvasSize();
      this.dibujar();
    });
  }

  ngOnDestroy(): void {
    if (this.canvas) {
      this.canvas.removeEventListener('wheel', () => {});
      this.canvas.removeEventListener('mousedown', () => {});
      this.canvas.removeEventListener('mousemove', () => {});
      this.canvas.removeEventListener('mouseup', () => {});
      this.canvas.removeEventListener('mouseleave', () => {});
      this.canvas.removeEventListener('click', () => {});
    }
  }

  adjustCanvasSize(): void {
    if (!this.canvas) return;
    const parent = this.canvas.parentElement;
    if (parent) {
      this.canvasWidth = parent.clientWidth;
      this.canvasHeight = parent.clientHeight;
      this.canvas.width = this.canvasWidth;
      this.canvas.height = this.canvasHeight;
    }
  }

  cargarMapa(): void {
    this.cargando = true;
    this.mensaje = '';

    this.http.get<MapaAsientos>(`${this.apiUrl}/evento/${this.eventoId}/zona/${this.zonaId}`).subscribe({
      next: (data) => {
        this.mapa = data;
        this.cargando = false;
        this.listaFilas = Object.keys(data.filas).sort((a, b) => {
          if (a.length === b.length) {
            return a.localeCompare(b);
          }
          return a.length - b.length;
        });
        this.calcularPosiciones();
        this.dibujar();
      },
      error: (err) => {
        this.cargando = false;
        this.mensaje = 'Error al cargar el mapa de asientos';
        this.tipoMensaje = 'danger';
        console.error(err);
      }
    });
  }

  calcularPosiciones(): void {
    this.asientosPos = [];
    if (!this.mapa || !this.listaFilas.length) return;

    const formacion = this.mapa.formacion || 'ESTADIO';

    if (formacion === 'CIRCULAR') {
      this.calcularPosicionesCircular();
    } else if (formacion === 'ARENA') {
      this.calcularPosicionesArena();
    } else {
      this.calcularPosicionesEstadio();
    }
  }

  calcularPosicionesEstadio(): void {
    const asientoAncho = 18;
    const asientoAlto = 18;
    const gap = 3;
    const filaGap = 8;
    const paddingX = 60;
    const paddingY = 100;
    const curvatura = 0.3;

    this.listaFilas.forEach((fila, filaIndex) => {
      const asientos = this.mapa!.filas[fila];
      const totalAsientos = asientos.length;
      const centroFila = totalAsientos / 2;

      asientos.forEach((asiento) => {
        const distanciaAlCentro = Math.abs(asiento.columna - centroFila - 0.5);
        const curvaY = distanciaAlCentro * curvatura * filaIndex;

        const x = paddingX + (asiento.columna - 1) * (asientoAncho + gap);
        const y = paddingY + filaIndex * (asientoAlto + filaGap) + curvaY;

        this.asientosPos.push({
          x: x, y: y, fila: fila,
          columna: asiento.columna, estado: asiento.estado
        });
      });
    });
  }

  calcularPosicionesCircular(): void {
    // Formación circular: pista en el centro, asientos en anillos concéntricos
    // Como la imagen de BTS: el escenario está en el medio y las gradas rodean
    const centerX = 350;
    const centerY = 350;
    const radioPista = 80; // Radio de la pista central
    const radioInicial = radioPista + 30; // Donde empiezan los asientos
    const gapRadial = 25; // Espacio entre anillos de asientos
    const asientoTamano = 14;

    this.listaFilas.forEach((fila, filaIndex) => {
      const asientos = this.mapa!.filas[fila];
      const radio = radioInicial + filaIndex * gapRadial;
      const totalAsientos = asientos.length;

      asientos.forEach((asiento, colIndex) => {
        // Distribuir asientos en un arco de 180 grados (semicírculo superior)
        // desde -90° hasta +90° (arriba del escenario)
        const anguloInicio = -Math.PI * 0.45; // -81 grados
        const anguloFin = Math.PI * 0.45; // +81 grados
        const angulo = anguloInicio + (colIndex / (totalAsientos - 1 || 1)) * (anguloFin - anguloInicio);

        const x = centerX + radio * Math.cos(angulo);
        const y = centerY + radio * Math.sin(angulo);

        this.asientosPos.push({
          x: x, y: y, fila: fila,
          columna: asiento.columna, estado: asiento.estado
        });
      });
    });
  }

  calcularPosicionesArena(): void {
    // Formación arena: escenario en frente (abajo), asientos en semicírculo
    // Como Bad Bunny: el escenario está al frente y las gradas rodean de frente a lados
    const centerX = 350;
    const centerY = 500; // Centro más abajo para dejar espacio al escenario
    const radioInicial = 100;
    const gapRadial = 25;
    const asientoTamano = 14;

    this.listaFilas.forEach((fila, filaIndex) => {
      const asientos = this.mapa!.filas[fila];
      const radio = radioInicial + filaIndex * gapRadial;
      const totalAsientos = asientos.length;

      asientos.forEach((asiento, colIndex) => {
        // Arco de 180 grados desde la izquierda hasta la derecha, pasando por arriba
        // El escenario queda abajo (frente)
        const anguloInicio = Math.PI * 0.05; // 9 grados (izquierda)
        const anguloFin = Math.PI * 0.95; // 171 grados (derecha)
        const angulo = anguloInicio + (colIndex / (totalAsientos - 1 || 1)) * (anguloFin - anguloInicio);

        const x = centerX + radio * Math.cos(angulo);
        const y = centerY + radio * Math.sin(angulo);

        this.asientosPos.push({
          x: x, y: y, fila: fila,
          columna: asiento.columna, estado: asiento.estado
        });
      });
    });
  }

  dibujar(): void {
    if (!this.ctx || !this.canvas) return;

    const ctx = this.ctx;
    const w = this.canvasWidth;
    const h = this.canvasHeight;

    // Limpiar
    ctx.clearRect(0, 0, w, h);
    ctx.fillStyle = this.colores.fondo;
    ctx.fillRect(0, 0, w, h);

    ctx.save();
    ctx.translate(this.offsetX, this.offsetY);
    ctx.scale(this.scale, this.scale);

    // Dibujar escenario según formación
    this.dibujarEscenario();

    // Dibujar asientos
    this.dibujarAsientos();

    ctx.restore();
  }

  dibujarEscenario(): void {
    if (!this.ctx) return;
    const ctx = this.ctx;
    const formacion = this.mapa?.formacion || 'ESTADIO';

    if (formacion === 'CIRCULAR') {
      // BTS: Pista circular en el centro
      const centerX = 350;
      const centerY = 350;
      const radio = 80;

      // Círculo de la pista
      const gradient = ctx.createRadialGradient(centerX, centerY, 0, centerX, centerY, radio);
      gradient.addColorStop(0, '#495057');
      gradient.addColorStop(1, '#6c757d');

      ctx.fillStyle = gradient;
      ctx.beginPath();
      ctx.arc(centerX, centerY, radio, 0, Math.PI * 2);
      ctx.fill();

      // Borde de la pista
      ctx.strokeStyle = '#343a40';
      ctx.lineWidth = 3;
      ctx.stroke();

      // Texto
      ctx.fillStyle = '#ffffff';
      ctx.font = 'bold 13px Arial';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText('PISTA', centerX, centerY - 10);
      ctx.fillText('CENTRAL', centerX, centerY + 10);

    } else if (formacion === 'ARENA') {
      // Bad Bunny: Escenario en frente (abajo)
      const centerX = 350;
      const stageY = 560;
      const w = 200;
      const h = 50;

      const gradient = ctx.createLinearGradient(centerX - w/2, stageY, centerX + w/2, stageY);
      gradient.addColorStop(0, '#dc3545');
      gradient.addColorStop(0.5, '#e74c5f');
      gradient.addColorStop(1, '#dc3545');

      ctx.fillStyle = gradient;
      ctx.beginPath();
      ctx.roundRect(centerX - w/2, stageY, w, h, 8);
      ctx.fill();

      ctx.strokeStyle = '#a71d2a';
      ctx.lineWidth = 2;
      ctx.stroke();

      ctx.fillStyle = '#ffffff';
      ctx.font = 'bold 14px Arial';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText('ESCENARIO', centerX, stageY + h/2);

    } else {
      // ESTADIO: Escenario/Cancha rectangular arriba
      const w = 500;
      const h = 40;
      const x = (this.asientosPos.length > 0 ? this.asientosPos[0].x : 60);
      const y = 30;

      const gradient = ctx.createLinearGradient(x, y, x + w, y);
      gradient.addColorStop(0, '#495057');
      gradient.addColorStop(0.5, '#6c757d');
      gradient.addColorStop(1, '#495057');

      ctx.fillStyle = gradient;
      ctx.beginPath();
      ctx.roundRect(x, y, w, h, 8);
      ctx.fill();

      ctx.fillStyle = '#ffffff';
      ctx.font = 'bold 14px Arial';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText('CANCHA / PISTA', x + w / 2, y + h / 2);
    }
  }

  dibujarAsientos(): void {
    if (!this.ctx) return;
    const ctx = this.ctx;

    const asientoAncho = 18;
    const asientoAlto = 18;

    this.asientosPos.forEach((asiento) => {
      let color = this.colores.ocupado;
      if (asiento.estado === 'DISPONIBLE') {
        color = this.colores.disponible;
      }

      // Verificar si está seleccionado
      if (asiento.fila === this.filaSeleccionada && asiento.columna === this.columnaSeleccionada) {
        color = this.colores.seleccionado;
      }

      // Dibujar asiento con borde redondeado
      ctx.fillStyle = color;
      ctx.beginPath();
      ctx.roundRect(asiento.x, asiento.y, asientoAncho, asientoAlto, 3);
      ctx.fill();

      // Borde
      ctx.strokeStyle = 'rgba(0,0,0,0.1)';
      ctx.lineWidth = 1;
      ctx.stroke();

      // Número del asiento (solo si zoom es suficiente)
      if (this.scale > 0.8) {
        ctx.fillStyle = this.colores.texto;
        ctx.font = '9px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(asiento.columna.toString(), asiento.x + asientoAncho / 2, asiento.y + asientoAlto / 2);
      }
    });

    // Dibujar labels de filas
    this.dibujarLabelsFilas();
  }

  dibujarLabelsFilas(): void {
    if (!this.ctx) return;
    const ctx = this.ctx;

    const seen = new Set<string>();
    this.asientosPos.forEach((asiento) => {
      if (!seen.has(asiento.fila)) {
        seen.add(asiento.fila);
        ctx.fillStyle = '#666';
        ctx.font = 'bold 11px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(asiento.fila, asiento.x - 20, asiento.y + 9);
      }
    });
  }

  onWheel(e: WheelEvent): void {
    e.preventDefault();
    const delta = e.deltaY > 0 ? 0.9 : 1.1;
    const newScale = Math.max(0.3, Math.min(3, this.scale * delta));

    // Zoom hacia el cursor
    const rect = this.canvas!.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    this.offsetX = mouseX - (mouseX - this.offsetX) * (newScale / this.scale);
    this.offsetY = mouseY - (mouseY - this.offsetY) * (newScale / this.scale);
    this.scale = newScale;

    this.dibujar();
  }

  onMouseDown(e: MouseEvent): void {
    if (e.button === 0) { // Click izquierdo
      this.isDragging = true;
      this.dragStartX = e.clientX;
      this.dragStartY = e.clientY;
      this.dragOffsetX = this.offsetX;
      this.dragOffsetY = this.offsetY;
    }
  }

  onMouseMove(e: MouseEvent): void {
    if (this.isDragging) {
      this.offsetX = this.dragOffsetX + (e.clientX - this.dragStartX);
      this.offsetY = this.dragOffsetY + (e.clientY - this.dragStartY);
      this.dibujar();
    }
  }

  onMouseUp(): void {
    this.isDragging = false;
  }

  onClick(e: MouseEvent): void {
    if (!this.canvas) return;

    const rect = this.canvas.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    // Convertir coordenadas del mouse a coordenadas del canvas
    const canvasX = (mouseX - this.offsetX) / this.scale;
    const canvasY = (mouseY - this.offsetY) / this.scale;

    const asientoAncho = 18;
    const asientoAlto = 18;

    // Buscar el asiento clickeado
    for (const asiento of this.asientosPos) {
      if (canvasX >= asiento.x && canvasX <= asiento.x + asientoAncho &&
          canvasY >= asiento.y && canvasY <= asiento.y + asientoAlto) {
        if (asiento.estado === 'DISPONIBLE') {
          this.filaSeleccionada = asiento.fila;
          this.columnaSeleccionada = asiento.columna;
          this.mensaje = `Asiento ${asiento.fila}${asiento.columna} seleccionado`;
          this.tipoMensaje = 'info';

          this.asientoSeleccionado.emit({
            fila: asiento.fila,
            columna: asiento.columna,
            zonaId: this.zonaId,
            precio: this.mapa?.precio || 0
          });

          this.dibujar();
        }
        break;
      }
    }
  }

  resetZoom(): void {
    this.scale = 1;
    this.offsetX = 0;
    this.offsetY = 0;
    this.dibujar();
  }

  estaSeleccionado(fila: string, columna: number): boolean {
    return this.filaSeleccionada === fila && this.columnaSeleccionada === columna;
  }

  getClaseAsiento(estado: string, fila: string, columna: number): string {
    if (this.estaSeleccionado(fila, columna)) {
      return 'asiento seleccionado';
    }
    return `asiento ${estado.toLowerCase()}`;
  }

  obtenerColumnasMaximas(): number {
    if (!this.mapa || !this.listaFilas.length) return 0;
    const primeraFila = this.mapa.filas[this.listaFilas[0]];
    return primeraFila ? primeraFila.length : 0;
  }

  getBadgeClass(formacion: string): string {
    const f = formacion || 'ESTADIO';
    if (f === 'CIRCULAR') return 'badge-formacion badge-circular';
    if (f === 'ARENA') return 'badge-formacion badge-arena';
    return 'badge-formacion badge-estadio';
  }
}
