package com.tienda.model;

/**
 * Modelo que representa un producto artesanal disponible en el mercado.
 */
public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String categoria;
    private String imagen; // Nombre de recurso, URL o identificador visual
    private String vendedorId;
    private String artesanoNombre;

    public Producto() {}

    public Producto(String id, String nombre, String descripcion, double precio, int stock, 
                    String categoria, String imagen, String vendedorId, String artesanoNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagen = imagen;
        this.vendedorId = vendedorId;
        this.artesanoNombre = artesanoNombre;
    }

    // Getters y Setters respetando convenciones camelCase
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(String vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getArtesanoNombre() {
        return artesanoNombre;
    }

    public void setArtesanoNombre(String artesanoNombre) {
        this.artesanoNombre = artesanoNombre;
    }

    /**
     * Comprueba si el producto tiene existencias en stock.
     */
    public boolean tieneStockDisponible() {
        return this.stock > 0;
    }

    /**
     * Formatea el precio con símbolo de moneda local.
     */
    public String obtenerPrecioFormateado() {
        return String.format("$%.2f MXN", precio);
    }
}
