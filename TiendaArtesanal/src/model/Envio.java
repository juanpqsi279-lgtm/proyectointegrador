package src.model;

import java.util.ArrayList;

public class Envio {
    private String id;
    private String estado; // e.g., "Preparando", "Enviado", "Entregado"
    private String fechaEstimada;
    private String numeroGuia;
    private ArrayList<producto> productos;

    public Envio(String id, String estado, String fechaEstimada, String numeroGuia) {
        this.id = id;
        this.estado = estado;
        this.fechaEstimada = fechaEstimada;
        this.numeroGuia = numeroGuia;
        this.productos = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getEstado() { return estado; }
    public String getFechaEstimada() { return fechaEstimada; }
    public String getNumeroGuia() { return numeroGuia; }
    public ArrayList<producto> getProductos() { return productos; }

    public void addProducto(producto p) {
        productos.add(p);
    }
}
