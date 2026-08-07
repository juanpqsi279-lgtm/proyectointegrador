package com.tienda.model;

/**
 * Enum que representa los métodos de pago disponibles en la tienda.
 */
public enum MetodoPago {
    TARJETA("Tarjeta de Crédito / Débito", "Pago seguro con procesamiento bancario directo"),
    TRANSFERENCIA("Transferencia SPEI / Bancaria", "Transferencia electrónica instantánea"),
    EFECTIVO("Pago en Efectivo contra Entrega", "Pago al recibir el producto en domicilio");

    private final String titulo;
    private final String descripcion;

    MetodoPago(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
