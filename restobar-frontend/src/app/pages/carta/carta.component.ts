import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { CarritoService } from '../../services/carrito.service';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-carta',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './carta.component.html',
    styleUrls: ['./carta.component.scss']
})
export class CartaComponent implements OnInit {
    productos: any[] = [];
    usuario: any = null;

    constructor(
        private productoService: ProductoService,
        public carritoService: CarritoService,
        private router: Router,
        private auth: AuthService
    ) { }

    ngOnInit() {
        this.cargarProductos();
        this.usuario = this.auth.getUsuario();
    }

    cargarProductos() {
        this.productoService.listarPublico().subscribe(data => {
            this.productos = data.filter(p => p.stock > 0);
        });
    }

    agregarAlCarrito(producto: any) {
        this.carritoService.agregar(producto);
        Swal.fire({
            icon: 'success',
            title: 'Agregado',
            text: `${producto.nombre} agregado al carrito`,
            timer: 1500,
            showConfirmButton: false
        });
    }

    cerrarSesion() {
        this.auth.cerrarSesion();
        this.usuario = null;
        this.router.navigate(['/']);
    }
}
