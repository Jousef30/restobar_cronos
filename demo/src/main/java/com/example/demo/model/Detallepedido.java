package com.example.demo.model;


import jakarta.persistence.*;

@Entity
@Table(name = "detallepedido")
public class Detallepedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;


    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio;

    public Detallepedido() {}

    public Detallepedido(Pedido pedido, Producto producto, Integer cantidad, Double precio) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    // Getters y setters
    public Long getId_detalle() {
        return id_detalle;
    }
    public void setId_detalle(Long id_detalle) {
        this.id_detalle = id_detalle;
    }
    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public Double getPrecio() {
        return precio;
    }
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
}
