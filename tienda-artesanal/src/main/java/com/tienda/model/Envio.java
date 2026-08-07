package com.tienda.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa el estado y costo de envío de un pedido.
 */
public class Envio {
    public enum EstadoEnvio {
        PENDIENTE("Pendiente de Recolección", "#D99B26"),
        EN_CAMINO("En Camino al Domicilio", "#2E6B9E"),
        ENTREGADO("Entregado con Éxito", "#3E8E41");

        private final String etiqueta;
        private final String colorHex;

        EstadoEnvio(String etiqueta, String colorHex) {
            this.etiqueta = etiqueta;
            this.colorHex = colorHex;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public String getColorHex() {
            return colorHex;
        }
    }

    private String idEnvio;
    private String direccionDestino;
    private EstadoEnvio estado;
    private double costo;
    private String fechaCreacion;

    public Envio(String idEnvio, String direccionDestino, double costo) {
        this.idEnvio = idEnvio;
        this.direccionDestino = direccionDestino;
        this.estado = EstadoEnvio.PENDIENTE;
        this.costo = costo;
        this.fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getCostoFormateado() {
        return String.format("$%.2f MXN", costo);
    }
}
