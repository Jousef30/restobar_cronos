import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CategoriaComponent } from './categorias/categoria.component';
import { ProductoComponent } from './producto/producto.component';
import { MesaComponent } from './mesas/mesa.component';
import { UsuarioComponent } from './usuario/usuario.component';
import { PedidoComponent } from './pedidos/pedido.component';
import { PromocionComponent } from './promocion/promocion.component';
import { ComprobanteComponent } from './comprobantes/comprobante.component';
import { DetallePedidoComponent } from './Detallepedido/detalle-pedido.component';
import { AuthGuard } from './services/auth.guard';
import { DashboardComponent } from './dashboard.component';
import { PromosWebComponent } from './pages/promosweb/promosweb.component';
import { InicioComponent } from './pages/inicio.component';
import { ReservasWebComponent } from './pages/reservaweb/reservasweb.component';
import { ReservaComponent } from './reserva/reserva.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { CartaComponent } from './pages/carta/carta.component';
import { CarritoComponent } from './pages/carrito/carrito.component';
import { PagoComponent } from './pages/pago/pago.component';


export const routes: Routes = [
    { path: '', component: InicioComponent },
    { path: 'promosweb', component: PromosWebComponent },
    { path: 'reservaweb', component: ReservasWebComponent },
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'carta', component: CartaComponent },
    { path: 'carrito', component: CarritoComponent },
    { path: 'pago', component: PagoComponent },

    {
        path: 'dashboard', component: DashboardComponent, children: [
            { path: 'categorias', component: CategoriaComponent, canActivate: [AuthGuard] },
            { path: 'productos', component: ProductoComponent, canActivate: [AuthGuard] },
            { path: 'mesas', component: MesaComponent, canActivate: [AuthGuard] },
            { path: 'usuarios', component: UsuarioComponent, canActivate: [AuthGuard] },
            { path: 'pedidos', component: PedidoComponent, canActivate: [AuthGuard] },
            { path: 'promociones', component: PromocionComponent, canActivate: [AuthGuard] },
            { path: 'comprobantes', component: ComprobanteComponent, canActivate: [AuthGuard] },
            { path: 'detallepedido', component: DetallePedidoComponent, canActivate: [AuthGuard] },
            { path: 'reservas', component: ReservaComponent, canActivate: [AuthGuard] },

        ]
    },



];
