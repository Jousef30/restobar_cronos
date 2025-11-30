import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class CarritoService {
    private items: any[] = [];

    constructor() {
        // Cargar del sessionStorage
        const stored = sessionStorage.getItem('carrito');
        if (stored) {
            this.items = JSON.parse(stored);
        }
    }

    agregar(producto: any) {
        const existe = this.items.find(i => i.id_producto === producto.id_producto);
        if (existe) {
            existe.cantidad++;
        } else {
            this.items.push({ ...producto, cantidad: 1 });
        }
        this.guardar();
    }

    quitar(idProducto: number) {
        this.items = this.items.filter(i => i.id_producto !== idProducto);
        this.guardar();
    }

    vaciar() {
        this.items = [];
        sessionStorage.removeItem('carrito');
    }

    obtenerItems() {
        return this.items;
    }

    obtenerCantidad() {
        return this.items.reduce((total, item) => total + item.cantidad, 0);
    }

    obtenerTotal() {
        return this.items.reduce((total, item) => total + (item.precio * item.cantidad), 0);
    }

    private guardar() {
        sessionStorage.setItem('carrito', JSON.stringify(this.items));
    }
}
