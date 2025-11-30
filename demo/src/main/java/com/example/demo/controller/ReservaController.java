package com.example.demo.controller;

import com.example.demo.model.Reserva;
import com.example.demo.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Reserva> listar() {
        return reservaService.listarReservas();
    }

    @GetMapping("/public")
    public List<Reserva> listarPublico() {
        return reservaService.listarReservas();
    }

    // Endpoint público para crear reservas desde web (sin autenticación)
    @PostMapping("/public")
    public Reserva crearPublico(@RequestBody Reserva reserva) {
        System.out.println("Reserva web recibida: " + reserva);
        return reservaService.crearReserva(reserva);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Reserva crear(@RequestBody Reserva reserva) {
        System.out.println("JSON recibido: " + reserva);
        return reservaService.crearReserva(reserva);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id) {
        return reservaService.obtenerReserva(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.actualizarReserva(id, reserva));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}
