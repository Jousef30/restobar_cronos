package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "comprobante")
public class Comprobante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_comprobante;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;


    private Timestamp fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

   
    public enum Tipo {
        BOLETA,
        FACTURA
    }

    // Constructores
    public Comprobante() {}

    public Comprobante(Pedido pedido, Tipo tipo) {
        this.pedido = pedido;
        this.fecha = new Timestamp(System.currentTimeMillis());  
        this.tipo = tipo;
    }

    // Getters y setters
    public Long getId_comprobante() {
        return id_comprobante;
    }
    public void setId_comprobante(Long id_comprobante) {
        this.id_comprobante = id_comprobante;
    }
    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public Timestamp getFecha() {
        return fecha;
    }
    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    public Tipo getTipo() {
        return tipo;
    }
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }


}
