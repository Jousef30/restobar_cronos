package com.example.demo.controller;

import com.example.demo.dto.PagoRequest;
import com.example.demo.dto.PagoResponse;
import com.example.demo.model.Detallepedido;
import com.example.demo.model.Mesa;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DetallepedidoRepository;
import com.example.demo.repository.MesaRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")
public class MercadoPagoController {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetallepedidoRepository detallepedidoRepository;

    @PostMapping("/procesar")
    public ResponseEntity<PagoResponse> procesarPago(@RequestBody PagoRequest request) {
        System.out.println("=== PROCESANDO PAGO CON MERCADO PAGO ===");
        System.out.println("Token recibido: " + request.getToken());
        System.out.println("Monto: " + request.getTransactionAmount());
        System.out.println("Payment Method ID: " + request.getPaymentMethodId());

        try {
            // Configurar Mercado Pago con el Access Token
            System.out.println("Access Token configurado: "
                    + (accessToken != null ? accessToken.substring(0, 20) + "..." : "NULL"));
            MercadoPagoConfig.setAccessToken(accessToken);

            // Crear el cliente de pagos
            PaymentClient client = new PaymentClient();

            // Preparar la información del pagador
            PaymentPayerRequest payerRequest = PaymentPayerRequest.builder()
                    .email((String) request.getPayer().get("email"))
                    .build();

            // Crear la solicitud de pago
            PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                    .transactionAmount(BigDecimal.valueOf(request.getTransactionAmount()))
                    .token(request.getToken())
                    .description("Pedido Restobar Kronos")
                    .installments(1)
                    .paymentMethodId(request.getPaymentMethodId())
                    .payer(payerRequest)
                    .build();

            // Procesar el pago con Mercado Pago
            Payment payment = client.create(paymentRequest);

            System.out.println("✅ Pago procesado con ID: " + payment.getId());
            System.out.println("Estado: " + payment.getStatus());
            System.out.println("Status Detail: " + payment.getStatusDetail());

            // Crear el pedido en la base de datos
            Pedido pedido = crearPedido(request, payment.getStatus());

            // Preparar la respuesta
            PagoResponse response = new PagoResponse();
            response.setIdPagoMP(payment.getId());
            response.setEstado(payment.getStatus());
            response.setStatusDetail(payment.getStatusDetail());
            response.setIdPedido(pedido.getId_pedido());

            if ("approved".equals(payment.getStatus())) {
                response.setMensaje("Pago aprobado exitosamente");
                System.out.println("✅ PAGO APROBADO - Pedido #" + pedido.getId_pedido());
            } else if ("rejected".equals(payment.getStatus())) {
                response.setMensaje("Pago rechazado: " + payment.getStatusDetail());
                System.out.println("❌ PAGO RECHAZADO: " + payment.getStatusDetail());
            } else {
                response.setMensaje("Pago pendiente");
                System.out.println("⏳ PAGO PENDIENTE");
            }

            return ResponseEntity.ok(response);

        } catch (com.mercadopago.exceptions.MPApiException apiEx) {
            System.err.println("❌ ERROR DE API MERCADO PAGO:");
            System.err.println("Status Code: " + apiEx.getStatusCode());
            System.err.println("Message: " + apiEx.getMessage());
            System.err.println("API Response: " + apiEx.getApiResponse());
            apiEx.printStackTrace();

            PagoResponse errorResponse = new PagoResponse();
            errorResponse.setEstado("error");
            errorResponse.setMensaje(
                    "Error de Mercado Pago: " + apiEx.getMessage() + " (Status: " + apiEx.getStatusCode() + ")");

            return ResponseEntity.status(500).body(errorResponse);

        } catch (Exception e) {
            System.err.println("❌ ERROR AL PROCESAR PAGO: " + e.getMessage());
            e.printStackTrace();

            PagoResponse errorResponse = new PagoResponse();
            errorResponse.setEstado("error");
            errorResponse.setMensaje("Error al procesar el pago: " + e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private Pedido crearPedido(PagoRequest request, String estadoPago) {
        Pedido pedido = new Pedido();

        // Asignar usuario si está logueado, sino usar usuario genérico
        if (request.getIdUsuario() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(request.getIdUsuario());
            usuario.ifPresent(pedido::setUsuario);
        } else {
            // Crear o buscar usuario "Invitado" para pedidos sin login
            Optional<Usuario> invitado = usuarioRepository.findByEmail("invitado@restobar.com");
            if (invitado.isPresent()) {
                pedido.setUsuario(invitado.get());
            } else {
                // Si no existe, usar el primer usuario (ajustar según tu lógica)
                Usuario primeraUsuario = usuarioRepository.findAll().get(0);
                pedido.setUsuario(primeraUsuario);
            }
        }

        // Asignar mesa "WEB" para pedidos online
        Optional<Mesa> mesaWeb = mesaRepository.findById(1L);
        mesaWeb.ifPresent(pedido::setMesa);

        // Asignar fecha y total
        pedido.setFecha(new Timestamp(System.currentTimeMillis()));
        pedido.setTotal(request.getTransactionAmount());

        // Asignar estado según el resultado del pago
        if ("approved".equals(estadoPago)) {
            pedido.setEstado(Pedido.Estado.PAGADO);
        } else if ("rejected".equals(estadoPago)) {
            pedido.setEstado(Pedido.Estado.PENDIENTE);
        } else {
            pedido.setEstado(Pedido.Estado.PENDIENTE);
        }

        // Guardar pedido en la base de datos
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        System.out.println("💾 Pedido guardado con ID: " + pedidoGuardado.getId_pedido());

        // NUEVO: Guardar los detalles del pedido (productos individuales)
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (PagoRequest.ItemCarrito item : request.getItems()) {
                Optional<Producto> productoOpt = productoRepository.findById(item.getId_producto());

                if (productoOpt.isPresent()) {
                    Detallepedido detalle = new Detallepedido();
                    detalle.setPedido(pedidoGuardado);
                    detalle.setProducto(productoOpt.get());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecio(item.getPrecio());

                    detallepedidoRepository.save(detalle);
                    System.out.println("   ✅ Detalle guardado: " + item.getNombre() + " x" + item.getCantidad());
                } else {
                    System.err.println("   ⚠️ Producto no encontrado: ID " + item.getId_producto());
                }
            }
        }

        return pedidoGuardado;
    }
}
