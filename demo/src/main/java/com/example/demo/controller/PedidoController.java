package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200") // Usado para permitir peticiones desde el frontend (Angular)
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Endpoint para listar pedidos
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    // Endpoint para crear un nuevo pedido
    @PostMapping
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getFecha() == null) {
            pedido.setFecha(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        return pedidoService.crearPedido(pedido);
    }

    // Endpoint para obtener un pedido por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        return pedido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un pedido
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido pedidoActualizado = pedidoService.actualizarPedido(id, pedido);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // Endpoint para eliminar un pedido
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
