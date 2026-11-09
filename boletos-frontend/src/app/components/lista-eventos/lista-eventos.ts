import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BoletosService } from '../../services/boletos';
import { AuthService } from '../../services/auth.service';
import { ComprarEntradaComponent } from '../comprar-entrada/comprar-entrada';

@Component({
  selector: 'app-lista-eventos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ComprarEntradaComponent],
  templateUrl: './lista-eventos.html',
  styleUrl: './lista-eventos.css'
})
export class ListaEventosComponent implements OnInit {
  eventos: any[] = [];
  cargando: boolean = true;
  eventoSeleccionado: any = null;
  mostrarModal: boolean = false;

  constructor(
    private boletosService: BoletosService,
    public authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.cargarEventos();
    } else {
      this.cargando = false;
    }
  }

  cargarEventos(): void {
    this.cargando = true;
    this.boletosService.obtenerEventos().subscribe({
      next: (data: any) => {
        this.eventos = data;
        this.cargando = false;
      },
      error: (err: any) => {
        console.error('Error al cargar eventos:', err);
        this.cargando = false;
      }
    });
  }

  abrirCompra(evento: any): void {
    this.eventoSeleccionado = evento;
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.eventoSeleccionado = null;
  }

  cerrarSesion(): void {
    this.authService.logout();
  }
}
