package com.example.demo.controller;

import com.example.demo.model.Promocion;
import com.example.demo.Service.PromocionService;
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
@RequestMapping("/api/promociones")
@CrossOrigin(origins = "http://localhost:4200")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    private static final Path UPLOAD_DIR = Paths.get(
            "C:/Users/Jousef/Documents/UTP/CICLO6/APLICATIVO_WEB_INTEGRADO/proyecto/uploads/promociones")
            .toAbsolutePath().normalize();

    // Endpoint para listar promociones
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Promocion> listarPromociones() {
        return promocionService.listarPromociones();
    }

    @GetMapping("/public")
    public List<Promocion> listarPublico() {
        return promocionService.listarPromociones();
    }

    // Endpoint para crear una nueva promoción
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Promocion crearPromocion(@RequestBody Promocion promocion) {
        return promocionService.crearPromocion(promocion);
    }

    // Endpoint para subir imagen de la promoción
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
            String urlPublica = "/uploads/promociones/" + safeName;
            return ResponseEntity.ok(urlPublica);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Endpoint para obtener una promoción por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Promocion> obtenerPromocionPorId(@PathVariable Long id) {
        Optional<Promocion> promocion = promocionService.obtenerPromocionPorId(id);
        return promocion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar una promoción existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Promocion> actualizarPromocion(@PathVariable Long id, @RequestBody Promocion promocion) {
        Promocion promocionActualizada = promocionService.actualizarPromocion(id, promocion);
        return ResponseEntity.ok(promocionActualizada);
    }

    // Endpoint para eliminar una promoción
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.eliminarPromocion(id);
        return ResponseEntity.noContent().build();
    }
}
