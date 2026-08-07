package com.tienda.model;

/**
 * Representa un elemento dentro del carrito de compras (Producto + Cantidad seleccionada).
 */
public class ItemCarrito {
    private Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public String getSubtotalFormateado() {
        return String.format("$%.2f MXN", getSubtotal());
    }
}
