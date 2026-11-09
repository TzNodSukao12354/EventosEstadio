import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BoletosService } from '../../services/boletos';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminComponent implements OnInit {
  // Datos
  eventos: any[] = [];
  estadios: any[] = [];
  zonas: any[] = [];

  // Formularios
  nuevoEvento: any = {
    nombreEvento: '',
    tipoEvento: 'concierto',
    equipoLocal: '',
    equipoVisitante: '',
    artistaPrincipal: '',
    estadioId: 1,
    fecha: '',
    hora: '',
    fase: ''
  };

  nuevoEstadio: any = {
    nombre: '',
    ciudad: '',
    pais: '',
    capacidad: 0,
    horaAperturaPuertas: 2
  };

  nuevaZona: any = {
    estadioId: 1,
    nombre: '',
    precio: 0,
    aforoMaximo: 0,
    aforoActual: 0
  };

  // Edición
  editandoEvento: any = null;
  editandoEstadio: any = null;
  editandoZona: any = null;

  // UI
  tabActiva: string = 'eventos';

  constructor(
    private boletosService: BoletosService,
    public authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.cargarTodo();
    }
  }

  cargarTodo(): void {
    this.cargarEventos();
    this.cargarEstadios();
    this.cargarZonas();
  }

  // ===== EVENTOS =====

  cargarEventos(): void {
    this.boletosService.obtenerEventos().subscribe({
      next: (data: any) => this.eventos = data,
      error: (err: any) => console.error('Error al cargar eventos:', err)
    });
  }

  crearEvento(): void {
    this.boletosService.crearEvento(this.nuevoEvento).subscribe({
      next: () => {
        this.cargarEventos();
        this.resetFormularioEvento();
        alert('✅ Evento creado exitosamente');
      },
      error: (err: any) => {
        console.error('Error al crear evento:', err);
        alert('❌ Error al crear evento');
      }
    });
  }

  editarEvento(evento: any): void {
    this.editandoEvento = { ...evento };
  }

  guardarEventoEditado(): void {
    if (!this.editandoEvento) return;
    this.boletosService.actualizarEvento(this.editandoEvento.id, this.editandoEvento).subscribe({
      next: () => {
        this.cargarEventos();
        this.editandoEvento = null;
        alert('✅ Evento actualizado exitosamente');
      },
      error: (err: any) => {
        console.error('Error al actualizar evento:', err);
        alert('❌ Error al actualizar evento');
      }
    });
  }

  eliminarEvento(id: number): void {
    if (!confirm('¿Estás seguro de eliminar este evento?')) return;
    this.boletosService.eliminarEvento(id).subscribe({
      next: () => {
        this.cargarEventos();
        alert('✅ Evento eliminado');
      },
      error: (err: any) => {
        console.error('Error al eliminar evento:', err);
        alert('❌ Error al eliminar evento');
      }
    });
  }

  cancelarEdicionEvento(): void {
    this.editandoEvento = null;
  }

  resetFormularioEvento(): void {
    this.nuevoEvento = {
      nombreEvento: '',
      tipoEvento: 'concierto',
      equipoLocal: '',
      equipoVisitante: '',
      artistaPrincipal: '',
      estadioId: 1,
      fecha: '',
      hora: '',
      fase: ''
    };
  }

  // ===== ESTADIOS =====

  cargarEstadios(): void {
    this.boletosService.obtenerEstadios().subscribe({
      next: (data: any) => this.estadios = data,
      error: (err: any) => console.error('Error al cargar estadios:', err)
    });
  }

  crearEstadio(): void {
    this.boletosService.crearEstadio(this.nuevoEstadio).subscribe({
      next: () => {
        this.cargarEstadios();
        this.resetFormularioEstadio();
        alert('✅ Estadio creado exitosamente');
      },
      error: (err: any) => {
        console.error('Error al crear estadio:', err);
        alert('❌ Error al crear estadio');
      }
    });
  }

  editarEstadio(estadio: any): void {
    this.editandoEstadio = { ...estadio };
  }

  guardarEstadioEditado(): void {
    if (!this.editandoEstadio) return;
    this.boletosService.actualizarEstadio(this.editandoEstadio.id, this.editandoEstadio).subscribe({
      next: () => {
        this.cargarEstadios();
        this.editandoEstadio = null;
        alert('✅ Estadio actualizado exitosamente');
      },
      error: (err: any) => {
        console.error('Error al actualizar estadio:', err);
        alert('❌ Error al actualizar estadio');
      }
    });
  }

  eliminarEstadio(id: number): void {
    if (!confirm('¿Estás seguro de eliminar este estadio?')) return;
    this.boletosService.eliminarEstadio(id).subscribe({
      next: () => {
        this.cargarEstadios();
        alert('✅ Estadio eliminado');
      },
      error: (err: any) => {
        console.error('Error al eliminar estadio:', err);
        alert('❌ Error al eliminar estadio');
      }
    });
  }

  cancelarEdicionEstadio(): void {
    this.editandoEstadio = null;
  }

  resetFormularioEstadio(): void {
    this.nuevoEstadio = {
      nombre: '',
      ciudad: '',
      pais: '',
      capacidad: 0,
      horaAperturaPuertas: 2
    };
  }

  // ===== ZONAS =====

  cargarZonas(): void {
    this.boletosService.obtenerZonas(0).subscribe({
      next: (data: any) => this.zonas = data,
      error: (err: any) => console.error('Error al cargar zonas:', err)
    });
  }

  crearZona(): void {
    this.boletosService.crearZona(this.nuevaZona).subscribe({
      next: () => {
        this.cargarZonas();
        this.resetFormularioZona();
        alert('✅ Zona creada exitosamente');
      },
      error: (err: any) => {
        console.error('Error al crear zona:', err);
        alert('❌ Error al crear zona');
      }
    });
  }

  editarZona(zona: any): void {
    this.editandoZona = { ...zona };
  }

  guardarZonaEditada(): void {
    if (!this.editandoZona) return;
    this.boletosService.actualizarZona(this.editandoZona.id, this.editandoZona).subscribe({
      next: () => {
        this.cargarZonas();
        this.editandoZona = null;
        alert('✅ Zona actualizada exitosamente');
      },
      error: (err: any) => {
        console.error('Error al actualizar zona:', err);
        alert('❌ Error al actualizar zona');
      }
    });
  }

  eliminarZona(id: number): void {
    if (!confirm('¿Estás seguro de eliminar esta zona?')) return;
    this.boletosService.eliminarZona(id).subscribe({
      next: () => {
        this.cargarZonas();
        alert('✅ Zona eliminada');
      },
      error: (err: any) => {
        console.error('Error al eliminar zona:', err);
        alert('❌ Error al eliminar zona');
      }
    });
  }

  cancelarEdicionZona(): void {
    this.editandoZona = null;
  }

  resetFormularioZona(): void {
    this.nuevaZona = {
      estadioId: 1,
      nombre: '',
      precio: 0,
      aforoMaximo: 0,
      aforoActual: 0
    };
  }

  cerrarSesion(): void {
    this.authService.logout();
  }

  getNombreEstadio(estadioId: number): string {
    const estadio = this.estadios.find(e => e.id === estadioId);
    return estadio ? estadio.nombre : `ID: ${estadioId}`;
  }
}
