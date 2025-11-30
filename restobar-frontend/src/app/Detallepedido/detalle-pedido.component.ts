import { Component, OnInit } from '@angular/core';
import { DetallePedidoService } from '../services/detalle-pedido.service';
import { PedidoService } from '../services/pedido.service';
import { ProductoService } from '../services/producto.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './detalle-pedido.component.html',
  styleUrls: ['./detalle-pedido.component.scss']
})
export class DetallePedidoComponent implements OnInit {

  detalles: any[] = [];
  pedidos: any[] = [];
  productos: any[] = [];

  detalle: any = {
    id_detalle: null,
    pedido: { id_pedido: '' },
    producto: { id_producto: '' },
    cantidad: 1,
    precio: 0,
    subtotal: 0
  };

  modalVisible = false;
  editando = false;

  // Detectar rol del usuario
  rol: string = '';

  constructor(
    private detalleService: DetallePedidoService,
    private pedidoService: PedidoService,
    private productoService: ProductoService
  ) { }

  ngOnInit(): void {
    this.obtenerRol();
    this.cargar();
  }

  obtenerRol() {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    this.rol = usuario.rol || '';
  }

  cargar() {
    this.detalleService.listar().subscribe(d => this.detalles = d);
    this.pedidoService.listar().subscribe(p => this.pedidos = p);
    this.productoService.listar().subscribe(pr => this.productos = pr);
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;
    this.detalle = {
      id_detalle: null,
      pedido: { id_pedido: '' },
      producto: { id_producto: '' },
      cantidad: 1,
      precio: 0,
      subtotal: 0
    };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  productoSeleccionado(event: any) {
    const id = event.target.value;
    const prod = this.productos.find(p => p.id_producto == id);

    if (prod) {
      this.detalle.precio = prod.precio;
      this.calcularSubtotal();
    }
  }

  calcularSubtotal() {
    this.detalle.subtotal = this.detalle.cantidad * this.detalle.precio;
  }

  guardar() {
    this.calcularSubtotal();

    if (this.editando) {
      this.detalleService.actualizar(this.detalle.id_detalle, this.detalle).subscribe(() => {
        Swal.fire({
          icon: 'success',
          title: 'Detalle actualizado correctamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.cargar();
        this.cerrarModal();
      });

    } else {
      this.detalleService.crear(this.detalle).subscribe(() => {
        Swal.fire({
          icon: 'success',
          title: 'Detalle registrado correctamente',
          timer: 1500,
          showConfirmButton: false
        });
        this.cargar();
        this.cerrarModal();
      });
    }
  }

  editar(d: any) {
    this.modalVisible = true;
    this.editando = true;
    this.detalle = JSON.parse(JSON.stringify(d));
  }

  eliminar(d: any) {
    Swal.fire({
      title: '¿Eliminar detalle?',
      text: 'No podrás revertir esta acción.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.detalleService.eliminar(d.id_detalle).subscribe(() => {
          Swal.fire({
            icon: 'success',
            title: 'Eliminado correctamente',
            timer: 1500,
            showConfirmButton: false
          });
          this.cargar();
        });
      }
    });
  }

}
