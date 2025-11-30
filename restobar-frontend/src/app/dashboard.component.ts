import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { jwtDecode } from 'jwt-decode';
import { AuthService } from './services/auth.service';
import { RouterLink } from '@angular/router';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  rol: string = '';

  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.obtenerRol();
    console.log("Dashboard cargado");
  }

  obtenerRol() {
    const token = this.auth.obtenerToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        console.log("Token decodificado:", decoded);

        // Detectar el campo del rol según cómo venga
        if (decoded.rol) {
          this.rol = decoded.rol;
        } else if (decoded.authorities && decoded.authorities.length > 0) {
          this.rol = decoded.authorities[0].authority;
        } else if (decoded.roles && decoded.roles.length > 0) {
          this.rol = decoded.roles[0];
        } else {
          console.warn("No se encontró el rol en el token");
        }

        console.log("Rol detectado:", this.rol);
      } catch (e) {
        console.error('Error al decodificar token', e);
      }
    }
  }

  logout() {
    this.auth.cerrarSesion();
    this.router.navigate(['/']);
  }

  mostrarBienvenida(): boolean {
    return this.router.url === '/dashboard';
  }
}
