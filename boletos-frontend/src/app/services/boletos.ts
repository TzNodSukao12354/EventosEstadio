import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BoletosService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // Login
  login(username: string, password: string): Observable<any> {
    const body = { username, password };
    return this.http.post(`${this.apiUrl}/auth/login`, body);
  }

  // Obtener eventos
  obtenerEventos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/eventos`);
  }

  // Obtener zonas de un estadio
  obtenerZonas(estadioId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/estadios/${estadioId}/zonas`);
  }

  // Comprar entrada (el interceptor agrega el token automáticamente)
  comprarEntrada(entrada: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/entradas`, entrada);
  }

  // Obtener entradas del usuario
  obtenerEntradas(): Observable<any> {
    return this.http.get(`${this.apiUrl}/entradas`);
  }

  // Reportar pérdida
  reportarPerdida(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/entradas/${id}/reportar-perdida`, null);
  }

  // Cancelar entrada
  cancelarEntrada(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/entradas/${id}/cancelar`, null);
  }

  // ===== MÉTODOS DE ADMINISTRACIÓN =====

  // Obtener estadios
  obtenerEstadios(): Observable<any> {
    return this.http.get(`${this.apiUrl}/estadios`);
  }

  // Crear evento
  crearEvento(evento: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/eventos`, evento);
  }

  // Actualizar evento
  actualizarEvento(id: number, evento: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/eventos/${id}`, evento);
  }

  // Eliminar evento
  eliminarEvento(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/eventos/${id}`);
  }

  // Crear estadio
  crearEstadio(estadio: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/estadios`, estadio);
  }

  // Actualizar estadio
  actualizarEstadio(id: number, estadio: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/estadios/${id}`, estadio);
  }

  // Eliminar estadio
  eliminarEstadio(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/estadios/${id}`);
  }

  // Crear zona
  crearZona(zona: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/zonas`, zona);
  }

  // Actualizar zona
  actualizarZona(id: number, zona: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/zonas/${id}`, zona);
  }

  // Eliminar zona
  eliminarZona(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/zonas/${id}`);
  }

  // Comprar entrada con asiento específico
  comprarEntradaConAsiento(entrada: any): Observable<any> {
    // Primero reservar el asiento
    const reserva = this.http.post(`${this.apiUrl}/api/asientos/reservar`, {
      eventoId: entrada.eventoId,
      zonaId: entrada.zonaId,
      fila: entrada.fila,
      columna: entrada.columna,
      documentoUsuario: entrada.documentoUsuario
    });

    // Luego crear la entrada
    return new Observable(observer => {
      reserva.subscribe({
        next: (reservaResp: any) => {
          this.http.post(`${this.apiUrl}/entradas`, {
            eventoId: entrada.eventoId,
            zonaId: entrada.zonaId,
            nombreUsuario: entrada.nombreUsuario,
            documentoUsuario: entrada.documentoUsuario,
            tipoEntrega: entrada.tipoEntrega,
            fila: entrada.fila,
            columna: entrada.columna
          }).subscribe({
            next: (entradaResp: any) => {
              observer.next({
                mensaje: 'Entrada comprada con asiento',
                fila: entrada.fila,
                columna: entrada.columna
              });
              observer.complete();
            },
            error: (err: any) => {
              observer.error(err);
            }
          });
        },
        error: (err: any) => {
          observer.error(err);
        }
      });
    });
  }
}
