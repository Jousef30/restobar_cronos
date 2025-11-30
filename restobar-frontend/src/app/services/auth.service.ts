import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private API = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(email: string, password: string) {
    return this.http.post(`${this.API}/login`, { email, password });
  }

  guardarToken(token: string) {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  guardarUsuario(usuario: any) {
    localStorage.setItem('usuario', JSON.stringify(usuario));
  }

  getUsuario() {
    const u = localStorage.getItem('usuario');
    return u ? JSON.parse(u) : null;
  }

  cerrarSesion() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    localStorage.removeItem('returnUrl');
  }

  estaLogueado(): boolean {
    return !!localStorage.getItem('token');
  }

  // Métodos para manejar returnUrl
  guardarReturnUrl(url: string) {
    localStorage.setItem('returnUrl', url);
  }

  obtenerReturnUrl(): string | null {
    return localStorage.getItem('returnUrl');
  }

  limpiarReturnUrl() {
    localStorage.removeItem('returnUrl');
  }

  // Métodos para verificar roles
  getRolUsuario(): string | null {
    const usuario = this.getUsuario();
    return usuario ? usuario.rol : null;
  }

  esAdmin(): boolean {
    return this.getRolUsuario() === 'ROLE_ADMIN';
  }

  esMozo(): boolean {
    return this.getRolUsuario() === 'ROLE_MOZO';
  }

  esCliente(): boolean {
    return this.getRolUsuario() === 'ROLE_CLIENTE';
  }

  // Verificar si el usuario tiene permisos de administración (Admin o Mozo)
  tienePermisosDashboard(): boolean {
    return this.esAdmin() || this.esMozo();
  }

  // Método para registrar nuevos usuarios (clientes)
  registrar(nombreCompleto: string, email: string, password: string, telefono: string) {
    const body = {
      nombreCompleto,
      email,
      password,
      telefono,
      rol: 'ROLE_CLIENTE' // Por defecto, los registros públicos son clientes
    };
    return this.http.post(`${this.API}/registro`, body);
  }
}

