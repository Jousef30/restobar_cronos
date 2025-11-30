package com.example.demo.controller;

import com.example.demo.model.Mesa;
import com.example.demo.Service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "http://localhost:4200")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // Endpoint para listar mesas
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Mesa> listarMesas() {
        return mesaService.listarMesas();
    }

    @GetMapping("/public")
    public List<Mesa> listarPublico() {
        return mesaService.listarMesas();
    }

    // Endpoint para crear una nueva mesa
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mesa crearMesa(@RequestBody Mesa mesa) {
        return mesaService.crearMesa(mesa);
    }

    // Endpoint para obtener una mesa por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Mesa> obtenerMesaPorId(@PathVariable Long id) {
        Optional<Mesa> mesa = mesaService.obtenerMesaPorId(id);
        return mesa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar una mesa existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Mesa> actualizarMesa(@PathVariable Long id, @RequestBody Mesa mesa) {
        Mesa mesaActualizada = mesaService.actualizarMesa(id, mesa);
        return ResponseEntity.ok(mesaActualizada);
    }

    // Endpoint para eliminar una mesa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
        return ResponseEntity.noContent().build();
    }
}
