package com.example.demo.controller;

public class AuthResponse {

    private String token;
    private Long idUsuario;
    private String email;
    private String nombreCompleto;
    private String rol;

    public AuthResponse(String token, Long idUsuario, String email, String nombreCompleto, String rol) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}