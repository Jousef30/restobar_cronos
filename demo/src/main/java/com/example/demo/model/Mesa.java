package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mesa;

    @Column(nullable = false, unique = true)
    private Integer numero_mesa;

    @Column(nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

  
    public enum Estado {
        LIBRE,
        RESERVADA,
        OCUPADA
    }

    // Constructores
    public Mesa() {}

    public Mesa(Integer numero_mesa, Integer capacidad, Estado estado) {
        this.numero_mesa = numero_mesa;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId_mesa() {
        return id_mesa;
    }

    public void setId_mesa(Long id_mesa) {
        this.id_mesa = id_mesa;
    }

    public Integer getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(Integer numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
