package com.example.demo.controller;

import com.example.demo.model.Comprobante;
import com.example.demo.Service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comprobantes")
@CrossOrigin(origins = "http://localhost:4200")  
public class ComprobanteController {
    @Autowired
    private ComprobanteService comprobanteService;

    // Endpoint para listar productos
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
    public List<Comprobante> listarComprobante() {
        return comprobanteService.listarComprobante();
    }

    // Endpoint para crear un nuevo producto
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
    public Comprobante crearComprobante(@RequestBody Comprobante comprobante) {
        if (comprobante.getFecha() == null) {
            comprobante.setFecha(new java.sql.Timestamp(System.currentTimeMillis())); 
        }
        return comprobanteService.crearComprobante(comprobante);
    }

    // Endpoint para obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
    public ResponseEntity<Comprobante> obtenerComprobantePorId(@PathVariable Long id) {
        Optional<Comprobante> comprobante = comprobanteService.obtenerComprobantePorId(id);
        return comprobante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
    public ResponseEntity<Comprobante> actualizarComprobante(@PathVariable Long id, @RequestBody Comprobante comprobante) {
        Comprobante comprobanteActualizado = comprobanteService.actualizarComprobante(id, comprobante);
        return ResponseEntity.ok(comprobanteActualizado);
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  
    public ResponseEntity<Void> eliminarComprobante(@PathVariable Long id) {
        comprobanteService.eliminarComprobante(id);
        return ResponseEntity.noContent().build();
    }
}
