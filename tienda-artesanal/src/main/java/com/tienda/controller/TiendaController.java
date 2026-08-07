package com.tienda.controller;

import com.tienda.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Controlador principal de la aplicación Tienda Artesanal (Patrón MVC).
 * Administra el catálogo de productos, carrito de compras, perfiles de comprador/vendedor
 * y notificaciones entre la lógica de negocio y la interfaz de usuario.
 */
public class TiendaController {

    private final ObservableList<Producto> catalogoProductos;
    private final ObservableList<ItemCarrito> carritoCompras;
    private Comprador compradorActual;
    private Vendedor vendedorActual;
    private final List<Vendedor> directorioVendedores;

    private Consumer<String> notificadorMensajes;
    private Consumer<String> cambiadorVista;

    private final AtomicInteger contadorProductos = new AtomicInteger(100);
    private final AtomicInteger contadorOrdenes = new AtomicInteger(5000);

    public TiendaController() {
        this.catalogoProductos = FXCollections.observableArrayList();
        this.carritoCompras = FXCollections.observableArrayList();
        this.directorioVendedores = new ArrayList<>();

        inicializarDatosSemilla();
    }

    /**
     * Carga datos iniciales realistas de productos y perfiles para simular un mercado artesanal vivo.
     */
    private void inicializarDatosSemilla() {
        // Perfil de Vendedor Inicial (Artesano)
        vendedorActual = new Vendedor(
            "VEND-001",
            "Artesanías Raíces de Oaxaca",
            "Maestro Mateo Morales",
            4.9,
            "contacto@raicesoaxaca.com | +52 951 555 1234",
            "Taller familiar fundado en 1984 dedicado a preservar el arte tradicional del barro negro, alebrijes y textiles de telar de pedal."
        );
        directorioVendedores.add(vendedorActual);

        Vendedor vendedor2 = new Vendedor(
            "VEND-002",
            "Joyería Alma de Plata",
            "Elena Vázquez",
            4.7,
            "elena.v@almaplata.com",
            "Creaciones únicas en plata y piedras semi-preciosas."
        );
        directorioVendedores.add(vendedor2);

        Vendedor vendedor3 = new Vendedor(
            "VEND-003",
            "Telares de la Sierra",
            "Doña Carmen",
            5.0,
            "contacto@telaressierra.com",
            "Especialistas en textiles hechos en telar de cintura con hilos teñidos naturalmente."
        );
        directorioVendedores.add(vendedor3);

        // Perfil de Comprador Inicial
        compradorActual = new Comprador(
            "COMP-001",
            "Sofía Mendoza Ruiz",
            "sofia.mendoza@email.com",
            "+52 55 4123 9876",
            "Av. Reforma 450, Int 3B, Cuauhtémoc, CDMX, C.P. 06600"
        );

        // Productos Iniciales del Catálogo
        Producto p1 = new Producto(
            "PROD-001",
            "Jarrón de Barro Negro Calado",
            "Pieza artesanal moldeada a mano y bruñida con piedra de cuarzo. Cocida en horno subterráneo de reducción.",
            850.00,
            12,
            "Cerámica",
            "barro_negro",
            vendedorActual.getId(),
            vendedorActual.getArtesanoNombre()
        );

        Producto p2 = new Producto(
            "PROD-002",
            "Alebrije Místico Jaguar Alado",
            "Tallado en madera de copal y pintado a mano con pigmentos orgánicos y patrones zaporitmos tradicionales.",
            1420.00,
            5,
            "Madera",
            "alebrije_jaguar",
            vendedorActual.getId(),
            vendedorActual.getArtesanoNombre()
        );

        Producto p3 = new Producto(
            "PROD-003",
            "Tapiz Zapoteca Lana 100% Natural",
            "Tejido en telar de madera con hilo teñido mediante grana cochinilla y añil orgánico.",
            2300.00,
            3,
            "Textil",
            "tapiz_zapoteca",
            vendedor3.getId(),
            vendedor3.getArtesanoNombre()
        );

        Producto p4 = new Producto(
            "PROD-004",
            "Collar de Plata Ley .925 y Ámbar",
            "Dije de filigrana con gema de ámbar auténtico de Simojovel, Chiapas. Incluye certificado.",
            1150.00,
            8,
            "Joyería",
            "collar_ambar",
            vendedor2.getId(),
            vendedor2.getArtesanoNombre()
        );

        Producto p5 = new Producto(
            "PROD-005",
            "Bolso de Cuero Repujado a Mano",
            "Piel genuina curtida al vegetal con relieves florales hechos con cincel artesanal.",
            1780.00,
            6,
            "Cuero",
            "bolso_cuero",
            vendedor2.getId(),
            vendedor2.getArtesanoNombre()
        );

        Producto p6 = new Producto(
            "PROD-006",
            "Cojín Telar de Cintura Huasteco",
            "Bordado tradicional con hilo de algodón sobre manta natural. Medida standard 45x45 cm.",
            640.00,
            15,
            "Textil",
            "cojin_telar",
            vendedor3.getId(),
            vendedor3.getArtesanoNombre()
        );

        catalogoProductos.addAll(p1, p2, p3, p4, p5, p6);
        vendedorActual.registrarProducto(p1);
        vendedorActual.registrarProducto(p2);
        
        vendedor2.registrarProducto(p4);
        vendedor2.registrarProducto(p5);
        
        vendedor3.registrarProducto(p3);
        vendedor3.registrarProducto(p6);

        // Semilla de Orden de Compra previa para el Historial
        List<ItemCarrito> itemsPrevios = List.of(new ItemCarrito(p1, 1));
        Envio envioPrevio = new Envio("ENV-8841", compradorActual.getDireccion(), 120.00);
        envioPrevio.setEstado(Envio.EstadoEnvio.ENTREGADO);
        compradorActual.agregarOrden(new Comprador.OrdenCompra(
            "ORD-9012",
            "02/08/2026 14:30",
            itemsPrevios,
            970.00,
            envioPrevio,
            MetodoPago.TARJETA
        ));
    }

    // --- MÉTODOS DEL CATÁLOGO ---

    public ObservableList<Producto> getCatalogoProductos() {
        return catalogoProductos;
    }

    /**
     * Filtra el catálogo por categoría y/o término de búsqueda en tiempo real.
     */
    public List<Producto> filtrarProductos(String categoria, String busqueda) {
        String query = busqueda == null ? "" : busqueda.toLowerCase().trim();
        String cat = categoria == null ? "Todos" : categoria;

        return catalogoProductos.stream()
            .filter(p -> cat.equals("Todos") || p.getCategoria().equalsIgnoreCase(cat))
            .filter(p -> query.isEmpty() || 
                    p.getNombre().toLowerCase().contains(query) || 
                    p.getDescripcion().toLowerCase().contains(query) ||
                    p.getArtesanoNombre().toLowerCase().contains(query))
            .toList();
    }

    // --- MÉTODOS DEL CARRITO DE COMPRAS ---

    public ObservableList<ItemCarrito> getCarritoCompras() {
        return carritoCompras;
    }

    public void agregarAlCarrito(Producto producto) {
        Optional<ItemCarrito> existente = carritoCompras.stream()
            .filter(item -> item.getProducto().getId().equals(producto.getId()))
            .findFirst();

        if (existente.isPresent()) {
            ItemCarrito item = existente.get();
            item.setCantidad(item.getCantidad() + 1);
            // Forzar actualización en lista observable
            int idx = carritoCompras.indexOf(item);
            carritoCompras.set(idx, item);
            notificarMensaje("✨ Se incrementó la cantidad de '" + producto.getNombre() + "' en el carrito.");
        } else {
            carritoCompras.add(new ItemCarrito(producto, 1));
            notificarMensaje("🛒 '" + producto.getNombre() + "' agregado al carrito.");
        }
    }

    public void modificarCantidadCarrito(ItemCarrito item, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            carritoCompras.remove(item);
            notificarMensaje("🗑️ Producto retirado del carrito.");
            return;
        }

        item.setCantidad(nuevaCantidad);
        int idx = carritoCompras.indexOf(item);
        if (idx >= 0) {
            carritoCompras.set(idx, item);
        }
    }

    public void eliminarDelCarrito(ItemCarrito item) {
        carritoCompras.remove(item);
        notificarMensaje("🗑️ '" + item.getProducto().getNombre() + "' eliminado del carrito.");
    }

    public void vaciarCarrito() {
        carritoCompras.clear();
    }

    public double calcularSubtotalCarrito() {
        return carritoCompras.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
    }

    public int obtenerCantidadTotalArticulosCarrito() {
        return carritoCompras.stream().mapToInt(ItemCarrito::getCantidad).sum();
    }

    public double calcularCostoEnvio() {
        return carritoCompras.isEmpty() ? 0.0 : 150.00; // Tarifa plana de envío artesanal
    }

    public double calcularTotalCarrito() {
        return calcularSubtotalCarrito() + calcularCostoEnvio();
    }

    // --- MÉTODOS DE CHECKOUT Y ENVÍO ---

    public boolean procesarCompra(String direccionEnvio, MetodoPago metodoPago) {
        if (carritoCompras.isEmpty()) {
            notificarMensaje("⚠️ El carrito está vacío. Agrega productos antes de realizar la compra.");
            return false;
        }

        if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
            notificarMensaje("⚠️ Por favor ingresa una dirección de envío válida.");
            return false;
        }

        if (metodoPago == null) {
            notificarMensaje("⚠️ Selecciona un método de pago.");
            return false;
        }

        // Descontar stock de los productos
        for (ItemCarrito item : carritoCompras) {
            Producto prod = item.getProducto();
            prod.setStock(prod.getStock() - item.getCantidad());
        }

        // Crear Envío
        String idEnvio = "ENV-" + (1000 + (int)(Math.random() * 9000));
        Envio nuevoEnvio = new Envio(idEnvio, direccionEnvio.trim(), calcularCostoEnvio());

        // Crear Orden de Compra
        String idOrden = "ORD-" + contadorOrdenes.incrementAndGet();
        String fechaHoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Comprador.OrdenCompra orden = new Comprador.OrdenCompra(
            idOrden,
            fechaHoy,
            new ArrayList<>(carritoCompras),
            calcularTotalCarrito(),
            nuevoEnvio,
            metodoPago
        );

        // Registrar en comprador
        compradorActual.agregarOrden(orden);
        compradorActual.setDireccion(direccionEnvio.trim());

        // Notificar y limpiar
        notificarMensaje("🎉 ¡Compra exitosa! Código de orden: " + idOrden + ". Envío programado.");
        vaciarCarrito();
        return true;
    }

    // --- MÉTODOS DE GESTIÓN DE VENDEDOR ---

    public boolean publicarProducto(String nombre, String descripcion, double precio, int stock, String categoria, String imagenUrl) {
        if (nombre == null || nombre.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty()) {
            notificarMensaje("⚠️ El nombre y la descripción son requeridos.");
            return false;
        }

        if (precio <= 0 || stock <= 0) {
            notificarMensaje("⚠️ El precio y el stock deben ser mayores a 0.");
            return false;
        }

        String nuevoId = "PROD-" + contadorProductos.incrementAndGet();
        Producto nuevo = new Producto(
            nuevoId,
            nombre.trim(),
            descripcion.trim(),
            precio,
            stock,
            categoria,
            imagenUrl != null && !imagenUrl.isEmpty() ? imagenUrl : "artesania_generica",
            vendedorActual.getId(),
            vendedorActual.getArtesanoNombre()
        );

        catalogoProductos.add(0, nuevo); // Agregar arriba en el catálogo
        vendedorActual.registrarProducto(nuevo);

        notificarMensaje("🌿 ¡Producto '" + nombre + "' publicado exitosamente en el mercado!");
        return true;
    }

    // --- PERFILES Y CALLBACKS ---

    public Comprador getCompradorActual() {
        return compradorActual;
    }

    public Vendedor getVendedorActual() {
        return vendedorActual;
    }

    public List<Vendedor> getDirectorioVendedores() {
        return directorioVendedores;
    }

    public Vendedor obtenerVendedorPorId(String id) {
        for (Vendedor v : directorioVendedores) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public void eliminarProductoVendedor(Producto p) {
        catalogoProductos.remove(p);
        
        for (Vendedor v : directorioVendedores) {
            if (v.getId().equals(p.getVendedorId())) {
                v.getProductosEnVenta().remove(p);
                break;
            }
        }
        
        carritoCompras.removeIf(item -> item.getProducto().getId().equals(p.getId()));
        
        notificarMensaje("🗑️ El producto '" + p.getNombre() + "' ha sido eliminado.");
    }

    public void setNotificadorMensajes(Consumer<String> notificadorMensajes) {
        this.notificadorMensajes = notificadorMensajes;
    }

    public void setCambiadorVista(Consumer<String> cambiadorVista) {
        this.cambiadorVista = cambiadorVista;
    }

    public void solicitarCambioVista(String nombreVista) {
        if (cambiadorVista != null) {
            cambiadorVista.accept(nombreVista);
        }
    }

    private void notificarMensaje(String mensaje) {
        if (notificadorMensajes != null) {
            notificadorMensajes.accept(mensaje);
        }
    }
}
