import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../services/producto.service';
import { CategoriaService } from '../services/categoria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-producto',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './producto.component.html',
  styleUrls: ['./producto.component.scss']
})
export class ProductoComponent implements OnInit {

  productos: any[] = [];
  categorias: any[] = [];

  producto: any = {
    id_producto: null,
    nombre: '',
    descripcion: '',
    precio: null,
    stock: null,
    id_categoria: null,
    imagen: ''
  };

  imagenSeleccionada: File | null = null;

  modalVisible = false;
  editando = false;

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService
  ) { }

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.productoService.listar().subscribe(
      data => this.productos = data,
      err => Swal.fire("Error", "No se pudieron cargar los productos", "error")
    );

    this.categoriaService.listar().subscribe(
      data => this.categorias = data,
      err => Swal.fire("Error", "No se pudieron cargar las categorías", "error")
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;
    this.producto = {
      nombre: '',
      descripcion: '',
      precio: null,
      stock: null,
      id_categoria: null,
      imagen: ''
    };
    this.imagenSeleccionada = null;
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  seleccionarImagen(event: any) {
    this.imagenSeleccionada = event.target.files[0];
  }

  guardar() {
    if (!this.producto.nombre || !this.producto.descripcion || !this.producto.precio || !this.producto.stock || !this.producto.id_categoria) {
      Swal.fire("Campos incompletos", "Llena todos los campos", "warning");
      return;
    }

    if (this.editando) {
      this.procesoGuardar(true);
    } else {
      this.procesoGuardar(false);
    }
  }

  procesoGuardar(editando: boolean) {
    console.log('=== PROCESO GUARDAR ===');
    console.log('Editando:', editando);
    console.log('Imagen seleccionada:', this.imagenSeleccionada);
    console.log('Producto actual:', this.producto);

    if (this.imagenSeleccionada) {
      console.log('→ Subiendo imagen...');
      this.productoService.subirImagen(this.imagenSeleccionada).subscribe(
        (url: any) => {
          console.log('✅ Imagen subida exitosamente. URL:', url);
          this.producto.imagen = url;
          console.log('Producto con imagen:', this.producto);
          editando ? this.actualizar() : this.crear();
        },
        (error) => {
          console.error('❌ Error al subir imagen:', error);
          Swal.fire("Error", "No se pudo subir la imagen", "error");
        }
      );
    } else {
      console.log('→ Sin imagen, guardando directamente...');
      editando ? this.actualizar() : this.crear();
    }
  }

  crear() {
    const data = {
      nombre: this.producto.nombre,
      descripcion: this.producto.descripcion,
      precio: this.producto.precio,
      stock: this.producto.stock,
      imagen: this.producto.imagen,
      categoria: { id_categoria: this.producto.id_categoria }
    };

    console.log('=== CREAR PRODUCTO ===');
    console.log('Data a enviar:', JSON.stringify(data, null, 2));

    this.productoService.crear(data).subscribe(
      (response) => {
        console.log('✅ Producto creado exitosamente:', response);
        Swal.fire("Éxito", "Producto registrado correctamente", "success");
        this.cargar();
        this.cerrarModal();
      },
      (error) => {
        console.error('❌ Error al crear producto:', error);
        Swal.fire("Error", "No se pudo crear el producto", "error");
      }
    );
  }

  actualizar() {
    const data = {
      nombre: this.producto.nombre,
      descripcion: this.producto.descripcion,
      precio: this.producto.precio,
      stock: this.producto.stock,
      imagen: this.producto.imagen,
      categoria: { id_categoria: this.producto.id_categoria }
    };

    console.log('=== ACTUALIZAR PRODUCTO ===');
    console.log('ID:', this.producto.id_producto);
    console.log('Data a enviar:', JSON.stringify(data, null, 2));

    this.productoService.actualizar(this.producto.id_producto, data).subscribe(
      (response) => {
        console.log('✅ Producto actualizado exitosamente:', response);
        Swal.fire("Actualizado", "Producto actualizado correctamente", "success");
        this.cargar();
        this.cerrarModal();
      },
      (error) => {
        console.error('❌ Error al actualizar producto:', error);
        Swal.fire("Error", "No se pudo actualizar", "error");
      }
    );
  }

  editar(p: any) {
    this.editando = true;
    this.modalVisible = true;

    this.producto = {
      id_producto: p.id_producto,
      nombre: p.nombre,
      descripcion: p.descripcion,
      precio: p.precio,
      stock: p.stock,
      id_categoria: p.categoria?.id_categoria,
      imagen: p.imagen || ''
    };
    this.imagenSeleccionada = null;
  }

  eliminar(id: number) {
    Swal.fire({
      title: "¿Eliminar producto?",
      text: "Esta acción no se puede revertir",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    }).then(result => {
      if (result.isConfirmed) {
        this.productoService.eliminar(id).subscribe(
          () => {
            Swal.fire("Eliminado", "Producto borrado correctamente", "success");
            this.cargar();
          },
          () => Swal.fire("Error", "No se pudo eliminar", "error")
        );
      }
    });
  }

}
