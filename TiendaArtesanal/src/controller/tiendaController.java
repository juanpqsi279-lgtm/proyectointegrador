package src.controller;
import src.model.producto;
import java.util.ArrayList;
public class tiendaController {
    private ArrayList<producto> carrito;
    public tiendaController() {
        carrito = new ArrayList<>();
    }
    public void agregarProducto(producto p) {
        carrito.add(p);
    }
    public void limpiarCarrito() {
        carrito.clear();
    }
    public double calcularTotal() {
        double total = 0;
        for (producto p : carrito) {
            total += p.getPrecio();
        }
        return total;
    }
    public ArrayList<producto> getCarrito() {
        return carrito;
    }
    public void eliminarProducto(producto p) {
        carrito.remove(p);
    }
}
