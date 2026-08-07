package com.tienda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa el perfil del comprador y su historial de compras.
 */
public class Comprador {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private List<OrdenCompra> historialCompras;

    public static class OrdenCompra {
        private String idOrden;
        private String fecha;
        private List<ItemCarrito> items;
        private double total;
        private Envio envio;
        private MetodoPago metodoPago;

        public OrdenCompra(String idOrden, String fecha, List<ItemCarrito> items, double total, Envio envio, MetodoPago metodoPago) {
            this.idOrden = idOrden;
            this.fecha = fecha;
            this.items = new ArrayList<>(items);
            this.total = total;
            this.envio = envio;
            this.metodoPago = metodoPago;
        }

        public String getIdOrden() { return idOrden; }
        public String getFecha() { return fecha; }
        public List<ItemCarrito> getItems() { return items; }
        public double getTotal() { return total; }
        public String getTotalFormateado() { return String.format("$%.2f MXN", total); }
        public Envio getEnvio() { return envio; }
        public MetodoPago getMetodoPago() { return metodoPago; }
    }

    public Comprador(String id, String nombre, String email, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.historialCompras = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<OrdenCompra> getHistorialCompras() { return historialCompras; }

    public void agregarOrden(OrdenCompra orden) {
        this.historialCompras.add(0, orden); // Agregar al inicio (más reciente primero)
    }
}
