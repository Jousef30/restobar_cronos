import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriaService } from '../services/categoria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categoria.component.html',
  styleUrl: './categoria.component.scss'
})
export class CategoriaComponent implements OnInit {

  categorias: any[] = [];
  nombre = "";

  editMode = false;
  selectedId: number | null = null;
  nombreEdit = "";

  constructor(private categoriaService: CategoriaService) {}

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.listar().subscribe(
      data => this.categorias = data,
      err => Swal.fire("Error", "No se pudieron cargar las categorías", "error")
    );
  }

  guardar() {
    if (this.nombre.trim() === "") {
      Swal.fire("Advertencia", "El nombre no puede estar vacío", "warning");
      return;
    }

    this.categoriaService.crear({ nombre: this.nombre }).subscribe(() => {
      Swal.fire("Éxito", "Categoría creada correctamente", "success");
      this.nombre = "";
      this.cargarCategorias();
    });
  }

  eliminar(id: number) {
    Swal.fire({
      title: "¿Eliminar categoría?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    }).then((result) => {
      if (result.isConfirmed) {
        this.categoriaService.eliminar(id).subscribe(() => {
          Swal.fire("Eliminado", "La categoría fue eliminada", "success");
          this.cargarCategorias();
        });
      }
    });
  }

  abrirModal(categoria: any) {
    this.editMode = true;
    this.selectedId = categoria.id_categoria;
    this.nombreEdit = categoria.nombre;
  }

  cerrarModal() {
    this.editMode = false;
    this.selectedId = null;
    this.nombreEdit = "";
  }

  actualizar() {
    if (this.selectedId == null) return;

    this.categoriaService.actualizar(this.selectedId, { nombre: this.nombreEdit }).subscribe(() => {
      Swal.fire("Actualizado", "Categoría actualizada correctamente", "success");
      this.cerrarModal();
      this.cargarCategorias();
    });
  }

}
