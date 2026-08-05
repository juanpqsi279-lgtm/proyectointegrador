package src.model;

import java.util.ArrayList;

public class Vendedor {
    private String id;
    private String nombre;
    private String descripcion;
    private String imagenPath;
    private double calificacion;
    private ArrayList<producto> productos;

    public Vendedor(String id, String nombre, String descripcion, String imagenPath, double calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
        this.calificacion = calificacion;
        this.productos = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenPath() { return imagenPath; }
    public double getCalificacion() { return calificacion; }
    public ArrayList<producto> getProductos() { return productos; }

    public void setProductos(ArrayList<producto> productos) {
        this.productos = productos;
    }
}
