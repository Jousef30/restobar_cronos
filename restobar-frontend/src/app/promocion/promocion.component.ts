import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { PromocionService } from '../services/promocion.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-promocion',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './promocion.component.html',
  styleUrls: ['./promocion.component.scss']
})
export class PromocionComponent implements OnInit {

  promociones: any[] = [];

  estados = ['ACTIVO', 'INACTIVO'];

  promocion: any = {
    titulo: '',
    descripcion: '',
    imagen: '',
    estado: 'ACTIVO'
  };

  imagenSeleccionada: File | null = null;

  modalVisible = false;
  editando = false;

  constructor(private promocionService: PromocionService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.promocionService.listar().subscribe(
      data => this.promociones = data
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;
    this.promocion = { titulo: '', descripcion: '', imagen: '', estado: 'ACTIVO' };
    this.imagenSeleccionada = null;
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  seleccionarImagen(event: any) {
    this.imagenSeleccionada = event.target.files[0];
  }

  guardar() {
    if (this.editando) {
      this.procesoGuardar(true);
    } else {
      this.procesoGuardar(false);
    }
  }

  procesoGuardar(editando: boolean) {
    if (this.imagenSeleccionada) {
      this.promocionService.subirImagen(this.imagenSeleccionada).subscribe(
        (url: any) => {
          this.promocion.imagen = url;
          editando ? this.actualizar() : this.crear();
        }
      );
    } else {
      editando ? this.actualizar() : this.crear();
    }
  }

  crear() {
    this.promocionService.crear(this.promocion).subscribe(() => {
      Swal.fire('Éxito', 'Promoción registrada correctamente', 'success');
      this.cargar();
      this.cerrarModal();
    });
  }

  actualizar() {
    this.promocionService.actualizar(this.promocion.id_carrusel, this.promocion).subscribe(() => {
      Swal.fire('Actualizado', 'La promoción fue modificada', 'success');
      this.cargar();
      this.cerrarModal();
    });
  }

  editar(p: any) {
    this.modalVisible = true;
    this.editando = true;
    this.promocion = JSON.parse(JSON.stringify(p));
    this.imagenSeleccionada = null;
  }

  eliminar(p: any) {
    Swal.fire({
      title: '¿Eliminar promoción?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d9534f',
      cancelButtonColor: '#333',
      confirmButtonText: 'Sí, eliminar'
    }).then(result => {
      if (result.isConfirmed) {
        this.promocionService.eliminar(p.id_carrusel).subscribe(() => {
          Swal.fire('Eliminado', 'La promoción fue eliminada', 'success');
          this.cargar();
        });
      }
    });
  }

}
