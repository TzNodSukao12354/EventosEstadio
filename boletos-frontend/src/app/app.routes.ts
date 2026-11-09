import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { ListaEventosComponent } from './components/lista-eventos/lista-eventos';
import { MisEntradasComponent } from './components/mis-entradas/mis-entradas';
import { AdminComponent } from './components/admin/admin';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'eventos', component: ListaEventosComponent },
  { path: 'mis-entradas', component: MisEntradasComponent },
  { path: 'admin', component: AdminComponent },
  { path: '**', redirectTo: '/login' }
];
