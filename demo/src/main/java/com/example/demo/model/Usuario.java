package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(nullable = false, length = 100)
    private String nombre_completo;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Se acepta en requests (write) pero no se envía en responses (read)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 9)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.CLIENTE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha_registro;

    public enum Rol { CLIENTE, MOZO, ADMIN }

    public Usuario() {}

    public Usuario(String nombre_completo, String email, String password, String telefono, Rol rol) {
        this.nombre_completo = nombre_completo;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.rol = rol;
    }

    // getters y setters (incluye getter/setter de password si quieres, pero password no será serializada)
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre_completo() { return nombre_completo; }
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public LocalDateTime getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(LocalDateTime fecha_registro) { this.fecha_registro = fecha_registro; }
}
