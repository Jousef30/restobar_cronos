package com.example.demo.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;  

    private Timestamp fechaRegistro;

    
    public enum Rol {
        ROLE_CLIENTE,
        ROLE_MOZO,
        ROLE_ADMIN
    }

    // Constructores
    public Usuario() {}

    public Usuario(String nombreCompleto, String email, String password, String telefono, Rol rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.rol = rol;
        this.fechaRegistro = new Timestamp(System.currentTimeMillis());  // Fecha actual al crear el usuario
    }

    // Getters y setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
