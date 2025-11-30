import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CarritoService } from '../../services/carrito.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-carrito',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './carrito.component.html',
    styleUrls: ['./carrito.component.scss']
})
export class CarritoComponent implements OnInit {
    items: any[] = [];
    total = 0;

    constructor(
        public carritoService: CarritoService,
        private router: Router
    ) { }

    ngOnInit() {
        this.cargarCarrito();
    }

    cargarCarrito() {
        this.items = this.carritoService.obtenerItems();
        this.total = this.carritoService.obtenerTotal();
    }

    quitar(idProducto: number) {
        this.carritoService.quitar(idProducto);
        this.cargarCarrito();
        Swal.fire({
            icon: 'success',
            title: 'Eliminado',
            text: 'Producto eliminado del carrito',
            timer: 1500,
            showConfirmButton: false
        });
    }

    procederAlPago() {
        if (this.items.length === 0) {
            Swal.fire('Carrito Vacío', 'Agrega productos antes de proceder al pago', 'warning');
            return;
        }
        this.router.navigate(['/pago']);
    }
}
