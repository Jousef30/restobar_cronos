package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "promocion")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrusel;

    @Column(nullable = true, length = 100)
    private String titulo;

    @Column(nullable = true, length = 255)
    private String descripcion;

    @Column(nullable = false, length = 255)
    private String imagen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Estado estado;

    
    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    // Constructores
    public Promocion() {}

    public Promocion(String titulo, String descripcion, String imagen, Estado estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId_carrusel() {
        return id_carrusel;
    }

    public void setId_carrusel(Long id_carrusel) {
        this.id_carrusel = id_carrusel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
