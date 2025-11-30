import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CarritoService } from '../../services/carrito.service';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

// Declarar MercadoPago del SDK cargado globalmente
declare const MercadoPago: any;

@Component({
  selector: 'app-pago',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './pago.component.html',
  styleUrls: ['./pago.component.scss']
})
export class PagoComponent implements OnInit, AfterViewInit, OnDestroy {
  items: any[] = [];
  total = 0;
  procesando = false;

  private mp: any;
  private bricksBuilder: any;
  private cardPaymentBrickController: any;

  constructor(
    private carritoService: CarritoService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) { }

  ngOnInit() {
    this.items = this.carritoService.obtenerItems();
    this.total = this.carritoService.obtenerTotal();

    if (this.items.length === 0) {
      Swal.fire('Carrito Vacío', 'No hay productos para procesar el pago', 'warning');
      this.router.navigate(['/carta']);
    }
  }

  ngAfterViewInit() {
    // Esperar un poco para asegurar que el DOM esté listo
    setTimeout(() => {
      this.initMercadoPago();
    }, 500);
  }

  ngOnDestroy() {
    // Limpiar el brick al salir del componente
    if (this.cardPaymentBrickController) {
      this.cardPaymentBrickController.unmount();
    }
  }

  initMercadoPago() {
    console.log('=== INICIALIZANDO MERCADO PAGO ===');
    console.log('Public Key:', environment.mercadoPagoPublicKey);
    console.log('Total a pagar:', this.total);

    try {
      // Inicializar SDK de Mercado Pago
      this.mp = new MercadoPago(environment.mercadoPagoPublicKey, {
        locale: 'es-PE'
      });

      this.bricksBuilder = this.mp.bricks();

      // Crear Brick de CardPayment
      this.bricksBuilder.create('cardPayment', 'cardPaymentBrick_container', {
        initialization: {
          amount: this.total
        },
        customization: {
          visual: {
            style: {
              theme: 'default'
            }
          },
          paymentMethods: {
            maxInstallments: 1 // Solo pago único, sin cuotas
          }
        },
        callbacks: {
          onReady: () => {
            console.log('✅ Brick de pago listo');
          },
          onSubmit: (formData: any) => {
            return this.procesarPago(formData);
          },
          onError: (error: any) => {
            console.error('❌ Error en Brick:', error);
          }
        }
      }).then((controller: any) => {
        this.cardPaymentBrickController = controller;
        console.log('✅ Brick montado exitosamente');
      });

    } catch (error) {
      console.error('❌ Error al inicializar Mercado Pago:', error);
      Swal.fire('Error', 'No se pudo cargar el formulario de pago', 'error');
    }
  }

  async procesarPago(formData: any): Promise<void> {
    console.log('=== PROCESANDO PAGO ===');
    console.log('Form Data:', formData);

    this.procesando = true;

    try {
      const usuario = this.authService.getUsuario();
      const token = localStorage.getItem('token');

      const pagoRequest = {
        token: formData.token,
        paymentMethodId: formData.payment_method_id,
        issuerId: formData.issuer_id,
        transactionAmount: this.total,
        items: this.items,
        payer: formData.payer,
        idUsuario: usuario?.idUsuario || null
      };

      console.log('Enviando al backend:', pagoRequest);

      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      });

      const response: any = await this.http.post(
        `${environment.backendUrl}/pagos/procesar`,
        pagoRequest,
        { headers }
      ).toPromise();

      console.log('✅ Respuesta del backend:', response);

      this.procesando = false;

      if (response.estado === 'approved') {
        Swal.fire({
          icon: 'success',
          title: '¡Pago Exitoso!',
          html: `
            <p>Tu pedido ha sido confirmado</p>
            <p><strong>ID Pedido:</strong> #${response.idPedido}</p>
            <p><strong>Total pagado:</strong> S/. ${this.total.toFixed(2)}</p>
            <p>Recibirás un correo de confirmación</p>
          `,
          confirmButtonText: 'Aceptar',
          confirmButtonColor: '#27ae60'
        }).then(() => {
          this.carritoService.vaciar();
          this.router.navigate(['/']);
        });
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Pago Rechazado',
          text: response.mensaje || 'El pago fue rechazado. Intenta con otra tarjeta.',
          confirmButtonColor: '#e74c3c'
        });
      }

    } catch (error: any) {
      console.error('❌ Error al procesar pago:', error);
      this.procesando = false;

      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.error?.mensaje || 'Ocurrió un error al procesar el pago',
        confirmButtonColor: '#e74c3c'
      });
    }
  }
}
