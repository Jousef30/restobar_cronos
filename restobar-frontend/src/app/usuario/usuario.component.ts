import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.scss']
})
export class UsuarioComponent implements OnInit {

  usuarios: any[] = [];

  usuario: any = {
    idUsuario: null,
    nombreCompleto: '',
    email: '',
    password: '',
    telefono: '',
    rol: 'ROLE_CLIENTE'
  };

  roles = ['ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_MOZO'];

  modalVisible = false;
  editando = false;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.cargar();
  }

  cargar() {
    this.usuarioService.listar().subscribe(
      data => this.usuarios = data,
      err => console.log("Error al cargar usuarios:", err)
    );
  }

  abrirModal() {
    this.modalVisible = true;
    this.editando = false;
    this.usuario = {
      nombreCompleto: '',
      email: '',
      password: '',
      telefono: '',
      rol: 'ROLE_MOZO'
    };
  }

  cerrarModal() {
    this.modalVisible = false;
  }

  guardar() {
    if (this.editando) {
      this.usuarioService.actualizar(this.usuario.idUsuario, this.usuario).subscribe(() => {
        Swal.fire({
          icon: 'success',
          title: 'Usuario actualizado',
          showConfirmButton: false,
          timer: 1500
        });
        this.cargar();
        this.cerrarModal();
      });
    } else {
      this.usuarioService.crear(this.usuario).subscribe(() => {
        Swal.fire({
          icon: 'success',
          title: 'Usuario registrado',
          showConfirmButton: false,
          timer: 1500
        });
        this.cargar();
        this.cerrarModal();
      });
    }
  }

  editar(u: any) {
    this.modalVisible = true;
    this.editando = true;
    this.usuario = { ...u };
    this.usuario.password = "";
  }

  eliminar(u: any) {
    const id = u.idUsuario;

    if (!id) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se encontró el ID del usuario'
      });
      return;
    }

    Swal.fire({
      title: '¿Eliminar usuario?',
      text: "Esta acción no se puede revertir",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e74c3c',
      cancelButtonColor: '#7f8c8d',
      confirmButtonText: 'Eliminar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.usuarioService.eliminar(id).subscribe(() => {
          Swal.fire({
            icon: 'success',
            title: 'Eliminado',
            timer: 1500,
            showConfirmButton: false
          });
          this.cargar();
        });
      }
    });
  }

}
