import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PromocionService } from '../../services/promocion.service';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-promosweb',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './promosweb.component.html',
  styleUrls: ['./promosweb.component.scss']
})
export class PromosWebComponent implements OnInit {

  // 👉 Lista donde se guardarán las promociones
  promociones: any[] = [];
  usuarioLogueado: any = null;

  constructor(
    private promocionService: PromocionService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    console.log("Cargando PromosWebComponent...");
    this.cargar();
    // Cargar usuario del localStorage
    this.usuarioLogueado = this.auth.getUsuario();
  }

  cargar() {
    this.promocionService.listarPublico().subscribe(
      data => this.promociones = data,
      error => console.error('Error al cargar promociones públicas:', error)
    );
  }

  cerrarSesion() {
    this.auth.cerrarSesion();
    this.usuarioLogueado = null;
    this.router.navigate(['/']);
  }

}
