package src.controller;

import src.model.producto;
import src.model.Vendedor;
import src.model.Comprador;
import src.model.Envio;
import src.model.MetodoPago;
import java.util.ArrayList;

public class tiendaController {
    private ArrayList<producto> carrito;
    private ArrayList<Vendedor> vendedores;
    private Comprador compradorActual;

    public tiendaController() {
        carrito = new ArrayList<>();
        vendedores = new ArrayList<>();
        inicializarDatosMock();
    }

    private void inicializarDatosMock() {
        // Inicializar Vendedores
        Vendedor v1 = new Vendedor("V01", "María Elena", "Artesana oaxaqueña con 20 años de experiencia en textiles.", "/assets/vendedor1.png", 4.8);
        producto p1 = new producto("Pulsera tejida", 50);
        producto p2 = new producto("Collar artesanal", 120);
        producto p3 = new producto("Bolsa bordada", 300);
        producto p4 = new producto("Rebozo de seda", 600);
        v1.getProductos().add(p1);
        v1.getProductos().add(p2);
        v1.getProductos().add(p3);
        v1.getProductos().add(p4);
        
        Vendedor v2 = new Vendedor("V02", "Juan Pérez", "Maestro alfarero de Tonalá, creando piezas únicas de barro.", "/assets/vendedor2.png", 4.9);
        producto p5 = new producto("Taza artesanal", 200);
        producto p6 = new producto("Jarrón de cerámica", 400);
        producto p7 = new producto("Alebrije tallado", 800);
        producto p8 = new producto("Caja de Olinalá", 350);
        v2.getProductos().add(p5);
        v2.getProductos().add(p6);
        v2.getProductos().add(p7);
        v2.getProductos().add(p8);
        
        vendedores.add(v1);
        vendedores.add(v2);

        // Inicializar Comprador
        compradorActual = new Comprador("C01", "Ana Gómez", "ana.gomez@email.com", "Av. de los Insurgentes Sur 123, CDMX");
        compradorActual.addMetodoPago(new MetodoPago("M01", "Tarjeta de Crédito", "4567", "Ana Gómez"));
        compradorActual.addMetodoPago(new MetodoPago("M02", "Tarjeta de Débito", "1234", "Ana Gómez"));
        compradorActual.addMetodoPago(new MetodoPago("M03", "PayPal", "ana.gomez@email.com", "Ana Gómez"));
        compradorActual.addMetodoPago(new MetodoPago("M04", "Mercado Pago", "ana.gomez@email.com", "Ana Gómez"));
        compradorActual.addMetodoPago(new MetodoPago("M05", "OXXO Pay", "Efectivo", "Ana Gómez"));
        compradorActual.addMetodoPago(new MetodoPago("M06", "Transferencia SPEI", "CLABE", "Ana Gómez"));

        Envio e1 = new Envio("E01", "Enviado", "10 de Agosto, 2026", "TRK-987654321");
        e1.addProducto(p6);
        e1.addProducto(p5);
        compradorActual.addEnvio(e1);
        
        Envio e2 = new Envio("E02", "Entregado", "1 de Agosto, 2026", "TRK-123456789");
        e2.addProducto(p1);
        compradorActual.addEnvio(e2);
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

    public ArrayList<Vendedor> getVendedores() {
        return vendedores;
    }

    public Comprador getCompradorActual() {
        return compradorActual;
    }
}
