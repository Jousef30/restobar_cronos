import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-root',
  imports: [RouterLink, CommonModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss'
})
export class InicioComponent implements OnInit {

  usuarioLogueado: any = null;

  constructor(
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    // Cargar usuario del localStorage
    this.usuarioLogueado = this.auth.getUsuario();
  }

  cerrarSesion() {
    this.auth.cerrarSesion();
    this.usuarioLogueado = null;
    this.router.navigate(['/']);
  }

  ngAfterViewInit() {
    let index = 0;
    const slides = document.querySelectorAll('.slide') as NodeListOf<HTMLElement>;

    function showSlide(i: number) {
      slides.forEach(s => s.classList.remove('active'));
      slides[i].classList.add('active');
    }

    setInterval(() => {
      index = (index + 1) % slides.length;
      showSlide(index);
    }, 3000);

    showSlide(0);
  }

}
