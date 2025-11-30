import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']

})
export class LoginComponent {

  email = '';
  password = '';

  constructor(private auth: AuthService, private router: Router) { }

  login() {
    this.auth.login(this.email, this.password).subscribe({
      next: (resp: any) => {
        // Guardar token
        this.auth.guardarToken(resp.token);

        // Guardar datos completos del usuario
        const usuario = {
          idUsuario: resp.idUsuario,
          email: resp.email,
          nombreCompleto: resp.nombreCompleto,
          rol: resp.rol
        };
        this.auth.guardarUsuario(usuario);

        Swal.fire({
          icon: 'success',
          title: 'Login exitoso',
          text: 'Bienvenido ' + resp.nombreCompleto,
          timer: 1500,
          showConfirmButton: false
        });

        // Redirigir según el rol del usuario
        setTimeout(() => {
          const rol = resp.rol;

          if (rol === 'ROLE_ADMIN' || rol === 'ROLE_MOZO') {
            // Administradores y mozos van al dashboard
            this.router.navigate(['/dashboard']);
          } else {
            // Clientes vuelven a la página de origen o a inicio
            const returnUrl = this.auth.obtenerReturnUrl() || '/';
            this.auth.limpiarReturnUrl(); // Limpiar después de usar
            this.router.navigate([returnUrl]);
          }
        }, 1200);
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Credenciales inválidas',
          text: 'Verifica tu usuario o contraseña'
        });
      }
    });
  }

}
