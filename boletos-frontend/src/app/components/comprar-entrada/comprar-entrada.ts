import { Component, Input, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BoletosService } from '../../services/boletos';
import { MapaAsientosComponent } from '../mapa-asientos/mapa-asientos';

@Component({
  selector: 'app-comprar-entrada',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MapaAsientosComponent],
  templateUrl: './comprar-entrada.html',
  styleUrl: './comprar-entrada.css'
})
export class ComprarEntradaComponent implements OnInit {
  @Input() evento: any = null;

  nombreUsuario: string = '';
  documentoUsuario: string = '';
  zonaId: number = 0;
  tipoEntrega: string = 'E-TICKET';
  mensaje: string = '';
  tipoMensaje: string = ''; // 'success' | 'danger'
  cargando: boolean = false;

  zonasDisponibles: any[] = [];
  
  // Asiento seleccionado desde el mapa
  asientoSeleccionado: { fila: string; columna: number; zonaId: number; precio: number } | null = null;

  constructor(private boletosService: BoletosService, @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId) && this.evento) {
      this.cargarZonas();
    }
  }

  cargarZonas(): void {
    // Cargar las zonas del estadio del evento
    this.boletosService.obtenerZonas(this.evento.estadioId).subscribe({
      next: (zonas: any[]) => {
        this.zonasDisponibles = zonas;
        if (zonas.length > 0) {
          this.zonaId = zonas[0].id;
        }
      },
      error: (err: any) => {
        console.error('Error al cargar zonas:', err);
      }
    });
  }

  onZonaChange(): void {
    // Cuando cambia la zona, recargar el mapa y actualizar el precio
    const zona = this.zonasDisponibles.find(z => z.id === this.zonaId);
    if (zona) {
      this.asientoSeleccionado = null;
    }
  }

  onAsientoSeleccionado(asiento: { fila: string; columna: number; zonaId: number; precio: number }): void {
    this.asientoSeleccionado = asiento;
    // Actualizar la zona seleccionada si cambió
    if (asiento.zonaId !== this.zonaId) {
      this.zonaId = asiento.zonaId;
    }
  }

  comprar(): void {
    this.mensaje = '';
    this.tipoMensaje = '';

    if (!this.nombreUsuario || !this.documentoUsuario || !this.zonaId) {
      this.mensaje = 'Por favor completa todos los campos.';
      this.tipoMensaje = 'danger';
      return;
    }

    if (!this.asientoSeleccionado) {
      this.mensaje = 'Por favor selecciona un asiento en el mapa.';
      this.tipoMensaje = 'danger';
      return;
    }

    this.cargando = true;

    const entrada = {
      eventoId: this.evento.id,
      zonaId: this.zonaId,
      nombreUsuario: this.nombreUsuario,
      documentoUsuario: this.documentoUsuario,
      tipoEntrega: this.tipoEntrega,
      fila: this.asientoSeleccionado.fila,
      columna: this.asientoSeleccionado.columna
    };

    this.boletosService.comprarEntradaConAsiento(entrada).subscribe({
      next: (response: any) => {
        this.cargando = false;
        if (response && response.mensaje && response.mensaje.startsWith('ERROR')) {
          this.mensaje = response.mensaje;
          this.tipoMensaje = 'danger';
        } else {
          this.mensaje = `¡Entrada comprada exitosamente! Asiento ${this.asientoSeleccionado?.fila}${this.asientoSeleccionado?.columna}. Revisa tu correo para la confirmación.`;
          this.tipoMensaje = 'success';
          // Limpiar formulario
          this.nombreUsuario = '';
          this.documentoUsuario = '';
          this.asientoSeleccionado = null;
        }
      },
      error: (err: any) => {
        this.cargando = false;
        this.mensaje = 'Error al procesar la compra. Intenta de nuevo.';
        this.tipoMensaje = 'danger';
        console.error(err);
      }
    });
  }

  cerrar(): void {
    window.location.reload();
  }
}
