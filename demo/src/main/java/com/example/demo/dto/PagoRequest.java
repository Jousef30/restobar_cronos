package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class PagoRequest {
    private String token;
    private String paymentMethodId;
    private String issuerId;
    private Double transactionAmount;
    private List<ItemCarrito> items;
    private Map<String, Object> payer;
    private Long idUsuario;

    // Clase interna para items del carrito
    public static class ItemCarrito {
        private Long id_producto;
        private String nombre;
        private Double precio;
        private Integer cantidad;

        // Getters y Setters
        public Long getId_producto() {
            return id_producto;
        }

        public void setId_producto(Long id_producto) {
            this.id_producto = id_producto;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public Map<String, Object> getPayer() {
        return payer;
    }

    public void setPayer(Map<String, Object> payer) {
        this.payer = payer;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
