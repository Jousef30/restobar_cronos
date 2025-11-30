package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private static final Path UPLOAD_DIR = Paths.get(
            "C:/Users/Jousef/Documents/UTP/CICLO6/APLICATIVO_WEB_INTEGRADO/proyecto/uploads/productos")
            .toAbsolutePath().normalize();

    // Endpoint para listar productos
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    // Endpoint PÚBLICO para listar productos
    @GetMapping("/public")
    public List<Producto> listarProductosPublico() {
        return productoService.listarProductos();
    }

    // Endpoint para crear un nuevo producto
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }

    // Endpoint para subir imagen del producto
    @PostMapping("/upload-imagen")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> uploadImage(@RequestParam("imagen") MultipartFile imagen) {
        try {
            if (imagen.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo vacío");
            }

            // Asegura que el directorio exista
            Files.createDirectories(UPLOAD_DIR);

            // Nombre de archivo seguro (sin caracteres raros)
            String original = imagen.getOriginalFilename();
            if (original == null)
                original = "archivo";
            String safeName = original.replaceAll("[^a-zA-Z0-9._-]", "_");

            // Guarda el archivo
            Path destino = UPLOAD_DIR.resolve(safeName);
            imagen.transferTo(destino.toFile());

            // URL pública que podrás usar en la BD/Frontend
            String urlPublica = "/uploads/productos/" + safeName;
            return ResponseEntity.ok(urlPublica);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Endpoint para obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
