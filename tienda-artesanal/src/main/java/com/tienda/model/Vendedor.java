package com.tienda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa la cuenta del vendedor/artesano en la plataforma.
 */
public class Vendedor {
    private String id;
    private String nombreTienda;
    private double calificacion; // Escala 1.0 a 5.0 estrellas
    private List<Producto> productosEnVenta;
    private String contacto;
    private String artesanoNombre;
    private String historiaTaller;

    public Vendedor(String id, String nombreTienda, String artesanoNombre, double calificacion, String contacto, String historiaTaller) {
        this.id = id;
        this.nombreTienda = nombreTienda;
        this.artesanoNombre = artesanoNombre;
        this.calificacion = calificacion;
        this.contacto = contacto;
        this.historiaTaller = historiaTaller;
        this.productosEnVenta = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombreTienda() { return nombreTienda; }
    public void setNombreTienda(String nombreTienda) { this.nombreTienda = nombreTienda; }

    public String getArtesanoNombre() { return artesanoNombre; }
    public void setArtesanoNombre(String artesanoNombre) { this.artesanoNombre = artesanoNombre; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }

    public List<Producto> getProductosEnVenta() { return productosEnVenta; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getHistoriaTaller() { return historiaTaller; }
    public void setHistoriaTaller(String historiaTaller) { this.historiaTaller = historiaTaller; }

    public void registrarProducto(Producto producto) {
        this.productosEnVenta.add(producto);
    }
}
