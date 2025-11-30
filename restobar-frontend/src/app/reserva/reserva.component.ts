import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { ReservaService } from '../services/reserva.service';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reserva.component.html',
  styleUrls: ['./reserva.component.scss']
})
export class ReservaComponent implements OnInit {

  reservas: any[] = [];

  reserva: any = {
    usuario: { idUsuario: null },
    mesa: { id_mesa: null },
    fecha: '',
    hora: '',
    estado: 'PENDIENTE'
  };

  estados = ['PENDIENTE', 'CONFIRMADA', 'CANCELADA'];

  modalVisible = false;
  editando = false;

  constructor(private reservaService: ReservaService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.reservaService.listar().subscribe(
      data => this.reservas = data
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;

    this.reserva = {
      usuario: { idUsuario: null },
      mesa: { id_mesa: null },
      fecha: '',
      hora: '',
      estado: 'PENDIENTE'
    };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  // Método para formatear la hora
  formatearHoraParaBackend(hora: string): string {
    if (!hora) return '';
    return hora.split(':').length === 3 ? hora : hora + ':00';
  }

  guardar() {
    const reservaParaEnviar = {
      ...this.reserva,
      hora: this.formatearHoraParaBackend(this.reserva.hora)
    };

    console.log("JSON enviado:", reservaParaEnviar);

    if (this.editando) {
      this.reservaService.actualizar(reservaParaEnviar.id_reserva, reservaParaEnviar).subscribe(() => {
        Swal.fire("Actualizado", "La reserva fue actualizada correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });
    } else {
      this.reservaService.crear(reservaParaEnviar).subscribe(() => {
        Swal.fire("Guardado", "La reserva fue registrada correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });
    }
  }

  editar(r: any) {
    this.editando = true;
    this.modalVisible = true;

    this.reserva = {
      id_reserva: r.id_reserva,
      usuario: { idUsuario: r.usuario.idUsuario },
      mesa: { id_mesa: r.mesa.id_mesa },
      fecha: r.fecha,
      hora: r.hora, // Mantener el formato original para edición
      estado: r.estado
    };
  }

  eliminar(id: number) {
    Swal.fire({
      title: "¿Eliminar reserva?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    }).then(res => {
      if (res.isConfirmed) {
        this.reservaService.eliminar(id).subscribe(() => {
          Swal.fire("Eliminado", "La reserva fue eliminada", "success");
          this.cargar();
        });
      }
    });
  }
}