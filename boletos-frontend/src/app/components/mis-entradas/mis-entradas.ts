import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BoletosService } from '../../services/boletos';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-mis-entradas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './mis-entradas.html',
  styleUrl: './mis-entradas.css'
})
export class MisEntradasComponent implements OnInit {
  entradas: any[] = [];
  cargando: boolean = true;
  mensaje: string = '';

  constructor(
    private boletosService: BoletosService,
    public authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.cargarEntradas();
    } else {
      this.cargando = false;
    }
  }

  cargarEntradas(): void {
    this.cargando = true;
    this.boletosService.obtenerEntradas().subscribe({
      next: (data: any) => {
        this.entradas = Array.isArray(data) ? data : [data];
        this.cargando = false;
      },
      error: (err: any) => {
        console.error('Error al cargar entradas:', err);
        this.mensaje = 'No se pudieron cargar tus entradas. Verifica tu sesión.';
        this.cargando = false;
      }
    });
  }

  reportarPerdida(id: number): void {
    if (!confirm('¿Estás seguro de reportar esta entrada como perdida? No se puede revertir.')) {
      return;
    }

    this.boletosService.reportarPerdida(id).subscribe({
      next: (response: any) => {
        alert(response);
        this.cargarEntradas();
      },
      error: (err: any) => {
        alert('Error al reportar la pérdida.');
      }
    });
  }

  cancelarEntrada(id: number): void {
    if (!confirm('¿Estás seguro de cancelar esta entrada? Se liberará el cupo de la zona.')) {
      return;
    }

    this.boletosService.cancelarEntrada(id).subscribe({
      next: (response: any) => {
        alert(response);
        this.cargarEntradas();
      },
      error: (err: any) => {
        alert('Error al cancelar la entrada.');
      }
    });
  }

  getEstadoBadge(estados: string): string {
    switch (estados) {
      case 'VIGENTE': return 'bg-success';
      case 'PERDIDA': return 'bg-danger';
      case 'USADA': return 'bg-secondary';
      case 'CANCELADA': return 'bg-dark';
      case 'REIMPRESA': return 'bg-warning text-dark';
      default: return 'bg-info';
    }
  }
}
