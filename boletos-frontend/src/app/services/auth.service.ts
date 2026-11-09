import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private platformId: Object;
  private router: Router;

  constructor(router: Router, @Inject(PLATFORM_ID) platformId: Object) {
    this.router = router;
    this.platformId = platformId;
  }

  private getStorage(): Storage | null {
    return isPlatformBrowser(this.platformId) ? localStorage : null;
  }

  // Iniciar sesión y guardar token + usuario
  login(token: string, username: string, role: string): void {
    const storage = this.getStorage();
    if (storage) {
      storage.setItem('token', token);
      storage.setItem('username', username);
      storage.setItem('role', role);
    }
    this.router.navigate(['/eventos']);
  }

  // Cerrar sesión y limpiar datos
  logout(): void {
    const storage = this.getStorage();
    if (storage) {
      storage.removeItem('token');
      storage.removeItem('username');
      storage.removeItem('role');
    }
    this.router.navigate(['/login']);
  }

  // Verificar si el usuario está autenticado
  isAuthenticated(): boolean {
    const storage = this.getStorage();
    return storage ? !!storage.getItem('token') : false;
  }

  // Obtener el token almacenado
  getToken(): string | null {
    const storage = this.getStorage();
    return storage ? storage.getItem('token') : null;
  }

  // Obtener el nombre de usuario
  getUsername(): string | null {
    const storage = this.getStorage();
    return storage ? storage.getItem('username') : null;
  }

  // Obtener el rol del usuario
  getRole(): string | null {
    const storage = this.getStorage();
    return storage ? storage.getItem('role') : null;
  }

  // Verificar si el usuario es administrador
  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }
}
