import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ComprobanteService } from '../services/comprobante.service';
import { PedidoService } from '../services/pedido.service';
import { DetallePedidoService } from '../services/detalle-pedido.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-comprobante',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './comprobante.component.html',
  styleUrls: ['./comprobante.component.scss']
})
export class ComprobanteComponent implements OnInit {

  comprobantes: any[] = [];
  pedidos: any[] = [];

  tipos = ['BOLETA', 'FACTURA'];

  comprobante: any = {
    id_comprobante: null,
    pedido: { id_pedido: null },
    tipo: 'BOLETA',
    fecha: null
  };

  modalVisible = false;
  editando = false;

  // Detectar rol del usuario
  rol: string = '';

  // Para impresión de boleta
  boletaVisible = false;
  boletaActual: any = null;
  detallesPedido: any[] = [];
  fechaHoraActual: Date = new Date();

  constructor(
    private comprobanteService: ComprobanteService,
    private pedidoService: PedidoService,
    private detallePedidoService: DetallePedidoService
  ) { }

  ngOnInit(): void {
    this.obtenerRol();
    this.cargar();
    this.cargarPedidos();
  }

  obtenerRol() {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    this.rol = usuario.rol || '';
  }

  cargar() {
    this.comprobanteService.listar().subscribe(
      data => {
        // FORMATEAR FECHA AQUÍ
        this.comprobantes = data.map(c => ({
          ...c,
          fecha: c.fecha ? c.fecha.substring(0, 10) : ''   // ← SOLO YYYY-MM-DD
        }));
      },
      err => console.error(err)
    );
  }

  cargarPedidos() {
    this.pedidoService.listar().subscribe(
      data => this.pedidos = data,
      err => console.error("Error cargando pedidos:", err)
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;
    this.comprobante = { pedido: { id_pedido: null }, tipo: 'BOLETA', fecha: null };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  guardar() {
    if (this.editando) {
      this.comprobanteService.actualizar(this.comprobante.id_comprobante, this.comprobante)
        .subscribe(() => {
          Swal.fire({
            icon: 'success',
            title: 'Actualizado correctamente',
            timer: 1500,
            showConfirmButton: false
          });
          this.cargar();
          this.cerrarModal();
        });

    } else {
      this.comprobanteService.crear(this.comprobante)
        .subscribe(() => {
          Swal.fire({
            icon: 'success',
            title: 'Registrado correctamente',
            timer: 1500,
            showConfirmButton: false
          });
          this.cargar();
          this.cerrarModal();
        });
    }
  }

  editar(c: any) {
    this.modalVisible = true;
    this.editando = true;

    this.comprobante = JSON.parse(JSON.stringify(c));
  }

  eliminar(c: any) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede revertir',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.comprobanteService.eliminar(c.id_comprobante).subscribe(() => {
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

  pedidoSeleccionado(event: any) {
    const id = event.target.value;
    const pedido = this.pedidos.find(p => p.id_pedido == id);

    if (pedido) {
      this.comprobante.total = pedido.total;
    }
  }

  // Método para abrir modal de boleta e imprimir
  imprimirBoleta(comprobante: any) {
    this.boletaActual = comprobante;
    this.fechaHoraActual = new Date(); // Hora actual del sistema

    // Cargar detalles del pedido
    this.detallePedidoService.listar().subscribe(detalles => {
      this.detallesPedido = detalles.filter(
        d => d.pedido?.id_pedido === comprobante.pedido?.id_pedido
      );
      this.boletaVisible = true;
    });
  }

  cerrarBoleta() {
    this.boletaVisible = false;
    this.boletaActual = null;
    this.detallesPedido = [];
  }

  imprimir() {
    window.print();
  }

  // Calcular subtotal (sin IGV)
  calcularSubtotal(): number {
    return this.detallesPedido.reduce((sum, detalle) => {
      return sum + (detalle.cantidad * detalle.precio);
    }, 0);
  }

  // Calcular IGV (18% del subtotal)
  calcularIGV(): number {
    const subtotal = this.calcularSubtotal();
    return subtotal * 0.18;
  }

  // Calcular total con IGV
  calcularTotalConIGV(): number {
    return this.calcularSubtotal() + this.calcularIGV();
  }

}
