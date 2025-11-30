import { Component, OnInit } from '@angular/core';
import { MesaService } from '../../services/mesa.service';
import { ReservaService } from '../../services/reserva.service';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-reservaweb',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reservasweb.component.html',
  styleUrls: ['./reservasweb.component.scss']
})
export class ReservasWebComponent implements OnInit {

  mesas: any[] = [];
  modalVisible = false;

  mesaSeleccionada: any = null;
  fecha = '';
  hora = '';

  usuarioLogueado: any = null;

  constructor(
    private mesaService: MesaService,
    private reservaService: ReservaService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.cargarMesas();

    // usuario del localStorage
    this.usuarioLogueado = this.auth.getUsuario();
  }

  cargarMesas() {
    this.mesaService.listarPublico().subscribe(data => {
      this.mesas = data;
    });
  }

  cerrarSesion() {
    this.auth.cerrarSesion();
    this.usuarioLogueado = null;
    this.router.navigate(['/']);
  }

  seleccionarMesa(mesa: any) {
    if (mesa.estado === 'RESERVADA') return;

    // Verificar si el usuario está logueado
    if (!this.auth.estaLogueado()) {
      // Guardar la URL actual para volver después del login
      this.auth.guardarReturnUrl('/reservaweb');
      // Redirigir al login
      this.router.navigate(['/login']);
      return;
    }

    // Si está logueado, mostrar modal de reserva
    this.mesaSeleccionada = mesa;
    this.modalVisible = true;
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  reservar() {
    const body = {
      usuario: { idUsuario: this.usuarioLogueado.idUsuario },
      mesa: { id_mesa: this.mesaSeleccionada.id_mesa },
      fecha: this.fecha,
      hora: this.hora + ':00',
      estado: 'PENDIENTE'
    };

    this.reservaService.crearPublico(body).subscribe(() => {
      alert('Reserva realizada correctamente');
      this.cerrarModal();
      this.cargarMesas();
    });
  }
}
