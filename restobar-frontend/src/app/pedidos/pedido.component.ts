import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PedidoService } from '../services/pedido.service';
import { MesaService } from '../services/mesa.service';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-pedido',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pedido.component.html',
  styleUrls: ['./pedido.component.scss']
})
export class PedidoComponent implements OnInit {

  pedidos: any[] = [];
  mesas: any[] = [];
  usuarios: any[] = [];

  estados = ['PENDIENTE', 'PREPARANDO', 'SERVIDO', 'PAGADO'];

  pedido: any = {
    id_pedido: null,
    mesa: { id_mesa: null },
    usuario: { idUsuario: null },
    total: 0,
    estado: 'PENDIENTE'
  };

  modalVisible = false;
  editando = false;

  // Detectar rol del usuario
  rol: string = '';

  constructor(
    private pedidoService: PedidoService,
    private mesaService: MesaService,
    private usuarioService: UsuarioService
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
    this.pedidoService.listar().subscribe(
      data => this.pedidos = data
    );

    this.mesaService.listar().subscribe(
      data => this.mesas = data
    );

    this.usuarioService.listar().subscribe(
      data => this.usuarios = data
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;

    this.pedido = {
      mesa: { id_mesa: null },
      usuario: { idUsuario: null },
      total: 0,
      estado: 'PENDIENTE'
    };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  guardar() {
    if (this.editando) {
      this.pedidoService.actualizar(this.pedido.id_pedido, this.pedido).subscribe(() => {
        Swal.fire("Actualizado", "Pedido actualizado correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });

    } else {
      this.pedidoService.crear(this.pedido).subscribe(() => {
        Swal.fire("Guardado", "Pedido registrado correctamente", "success");
        this.cargar();
        this.cerrarModal();
      });
    }
  }

  editar(p: any) {
    this.modalVisible = true;
    this.editando = true;
    this.pedido = JSON.parse(JSON.stringify(p));
  }

  // Método para mozo: solo cambiar estado del pedido
  cambiarEstado(pedido: any, nuevoEstado: string) {
    const pedidoActualizado = { ...pedido, estado: nuevoEstado };

    this.pedidoService.actualizar(pedido.id_pedido, pedidoActualizado).subscribe(() => {
      Swal.fire({
        icon: 'success',
        title: 'Estado actualizado',
        text: `Pedido cambió a ${nuevoEstado}`,
        timer: 2000,
        showConfirmButton: false
      });
      this.cargar();
    });
  }

  eliminar(p: any) {
    Swal.fire({
      title: "¿Eliminar pedido?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Eliminar",
      cancelButtonText: "Cancelar"
    }).then(res => {
      if (res.isConfirmed) {
        this.pedidoService.eliminar(p.id_pedido).subscribe(() => {
          Swal.fire("Eliminado", "Pedido eliminado correctamente", "success");
          this.cargar();
        });
      }
    });
  }

}
