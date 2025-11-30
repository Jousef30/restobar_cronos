package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private Timestamp fecha;

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    
    public enum Estado {
        PENDIENTE,
        PREPARANDO,
        SERVIDO,
        PAGADO
    }

    // Constructores
    public Pedido() {}

    public Pedido(Mesa mesa, Usuario usuario, Double total, Estado estado) {
        this.mesa = mesa;
        this.usuario = usuario;
        this.fecha = new Timestamp(System.currentTimeMillis());  
        this.total = total;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId_pedido() {
        return id_pedido;
    }
    public void setId_pedido(Long id_pedido) {
        this.id_pedido = id_pedido;
    }
    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }

}
