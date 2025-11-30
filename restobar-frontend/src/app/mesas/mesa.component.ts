import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { MesaService } from '../services/mesa.service';

@Component({
  selector: 'app-mesas',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './mesa.component.html',
  styleUrls: ['./mesa.component.scss']
})
export class MesaComponent implements OnInit {

  mesas: any[] = [];

  mesa: any = {
    id_mesa: null,
    numero_mesa: null,
    capacidad: null,
    estado: 'LIBRE'
  };

  estados = ['LIBRE', 'RESERVADA', 'OCUPADA'];

  modalVisible = false;
  editando = false;

  // Detectar rol del usuario
  rol: string = '';

  constructor(private mesaService: MesaService) { }

  ngOnInit(): void {
    this.obtenerRol();
    this.cargar();
  }

  obtenerRol() {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    this.rol = usuario.rol || '';
  }

  cargar() {
    this.mesaService.listar().subscribe(
      data => this.mesas = data
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;

    this.mesa = {
      numero_mesa: '',
      capacidad: '',
      estado: 'LIBRE'
    };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  guardar() {
    if (this.editando) {
      this.mesaService.actualizar(this.mesa.id_mesa, this.mesa).subscribe(() => {
        Swal.fire("Actualizado", "La mesa fue actualizada correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });

    } else {
      this.mesaService.crear(this.mesa).subscribe(() => {
        Swal.fire("Guardado", "La mesa fue registrada correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });
    }
  }

  editar(m: any) {
    this.editando = true;
    this.modalVisible = true;
    this.mesa = { ...m };
  }

  // Método para mozo: solo cambiar estado
  cambiarEstado(mesa: any, nuevoEstado: string) {
    const mesaActualizada = { ...mesa, estado: nuevoEstado };

    this.mesaService.actualizar(mesa.id_mesa, mesaActualizada).subscribe(() => {
      Swal.fire({
        icon: 'success',
        title: 'Estado actualizado',
        text: `Mesa ${mesa.numero_mesa} ahora está ${nuevoEstado}`,
        timer: 2000,
        showConfirmButton: false
      });
      this.cargar();
    });
  }

  eliminar(id: number) {
    Swal.fire({
      title: "¿Eliminar mesa?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    }).then(res => {
      if (res.isConfirmed) {
        this.mesaService.eliminar(id).subscribe(() => {
          Swal.fire("Eliminado", "La mesa fue eliminada", "success");
          this.cargar();
        });
      }
    });
  }
}
