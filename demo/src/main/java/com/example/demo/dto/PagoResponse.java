package com.example.demo.dto;

public class PagoResponse {
    private String estado;
    private String mensaje;
    private Long idPedido;
    private Long idPagoMP; // ID del pago en Mercado Pago
    private String statusDetail;

    // Constructores
    public PagoResponse() {
    }

    public PagoResponse(String estado, String mensaje, Long idPedido) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.idPedido = idPedido;
    }

    // Getters y Setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdPagoMP() {
        return idPagoMP;
    }

    public void setIdPagoMP(Long idPagoMP) {
        this.idPagoMP = idPagoMP;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }
}
