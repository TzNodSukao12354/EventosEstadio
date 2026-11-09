import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BoletosService } from '../../services/boletos';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  error: string = '';
  cargando: boolean = false;

  constructor(
    private boletosService: BoletosService,
    private authService: AuthService
  ) {}

  iniciarSesion(): void {
    this.error = '';
    this.cargando = true;

    if (!this.username || !this.password) {
      this.error = 'Por favor ingresa usuario y contraseña.';
      this.cargando = false;
      return;
    }

    this.boletosService.login(this.username, this.password).subscribe({
      next: (response: any) => {
        this.cargando = false;
        // El backend devuelve: { token, username, role }
        this.authService.login(response.token, response.username, response.role);
      },
      error: (err: any) => {
        this.cargando = false;
        if (err.status === 401) {
          this.error = 'Usuario o contraseña incorrectos.';
        } else {
          this.error = 'Error al conectar con el servidor. Verifica que el backend esté corriendo.';
        }
      }
    });
  }
}
