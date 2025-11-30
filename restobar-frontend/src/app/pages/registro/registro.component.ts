import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-registro',
    standalone: true,
    imports: [FormsModule, RouterLink],
    templateUrl: './registro.component.html',
    styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {

    nombreCompleto = '';
    email = '';
    password = '';
    telefono = '';

    constructor(
        private auth: AuthService,
        private router: Router
    ) { }

    registrar() {
        // Validación básica
        if (!this.nombreCompleto || !this.email || !this.password) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor completa todos los campos requeridos'
            });
            return;
        }

        // Llamar al servicio de registro
        this.auth.registrar(this.nombreCompleto, this.email, this.password, this.telefono)
            .subscribe({
                next: (resp: any) => {
                    Swal.fire({
                        icon: 'success',
                        title: '¡Registro exitoso!',
                        text: 'Tu cuenta ha sido creada. Por favor inicia sesión.',
                        timer: 2000,
                        showConfirmButton: false
                    });

                    setTimeout(() => {
                        this.router.navigate(['/login']);
                    }, 2000);
                },
                error: (err) => {
                    console.error('Error en registro:', err);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al registrarse',
                        text: err.error?.message || 'Ya existe un usuario con ese email o hubo un error en el servidor'
                    });
                }
            });
    }
}
