package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_categoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Categoria() {}
    public Categoria(String nombre) { this.nombre = nombre; }

    public Long getId_categoria() { return id_categoria; }
    public void setId_categoria(Long id_categoria) { this.id_categoria = id_categoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
