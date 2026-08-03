package src.view;

import src.model.producto;
import src.controller.tiendaController;

import javax.swing.*;
import java.awt.*;

public class tiendaApp {
    private JFrame ventana;
    private DefaultListModel<producto> catalogoModel;
    private DefaultListModel<producto> carritoModel;
    private tiendaController controller;

    public tiendaApp() {
        controller = new tiendaController();

        ventana = new JFrame("Tienda Artesanal");
        ventana.setSize(600, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new GridLayout(1, 2));

        // Catálogo
        catalogoModel = new DefaultListModel<>();
        catalogoModel.addElement(new producto("Pulsera tejida", 50));
        catalogoModel.addElement(new producto("Collar artesanal", 120));
        catalogoModel.addElement(new producto("Bolsa bordada", 300));
        catalogoModel.addElement(new producto("Vesícula de Macías", 5600000));
        catalogoModel.addElement(new producto("Grecia", 88000));
        catalogoModel.addElement(new producto("Uriel", 1));
        catalogoModel.addElement(new producto("Perez", 210000));
        catalogoModel.addElement(new producto("Laptop de Perez", 10));
        catalogoModel.addElement(new producto("Cable artesanal", 100));
        catalogoModel.addElement(new producto("Blindaje artesanal", 4500000 ));
        JList<producto> listaCatalogo = new JList<>(catalogoModel);

        // Carrito
        carritoModel = new DefaultListModel<>();
        JList<producto> listaCarrito = new JList<>(carritoModel);

        // Botón agregar
        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.addActionListener(e -> {
            producto seleccionado = listaCatalogo.getSelectedValue();
            if (seleccionado != null) {
                controller.agregarProducto(seleccionado);
                carritoModel.addElement(seleccionado);
            }
        });

        // Botón finalizar compra
        JButton btnFinalizar = new JButton("Finalizar compra");
        btnFinalizar.addActionListener(e -> {
            double total = controller.calcularTotal();
            JOptionPane.showMessageDialog(ventana, "Total a pagar: $" + total);
            controller.limpiarCarrito();
            carritoModel.clear();
        });

        // Botón eliminar
        JButton btnEliminar = new JButton("Eliminar del carrito");
        btnEliminar.addActionListener(e -> {
            producto seleccionado = listaCarrito.getSelectedValue();
            if (seleccionado != null) {
                controller.eliminarProducto(seleccionado);
                carritoModel.removeElement(seleccionado);
            }
        });

        // Paneles
        JPanel panelCatalogo = new JPanel(new BorderLayout());
        JPanel panelCarrito = new JPanel(new BorderLayout());

        // Etiquetas personalizadas
        JLabel lblCatalogo = new JLabel("Catálogo");
        lblCatalogo.setFont(new Font("Arial", Font.BOLD, 16));
        lblCatalogo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblCarrito = new JLabel("Carrito");
        lblCarrito.setFont(new Font("Arial", Font.BOLD, 16));
        lblCarrito.setHorizontalAlignment(SwingConstants.CENTER);

        panelCatalogo.add(lblCatalogo, BorderLayout.NORTH);
        panelCatalogo.add(new JScrollPane(listaCatalogo), BorderLayout.CENTER);
        panelCatalogo.add(btnAgregar, BorderLayout.SOUTH);

        panelCarrito.add(lblCarrito, BorderLayout.NORTH);
        panelCarrito.add(new JScrollPane(listaCarrito), BorderLayout.CENTER);

        // Panel de botones del carrito
        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 2));
        panelBotonesCarrito.add(btnEliminar);
        panelBotonesCarrito.add(btnFinalizar);
        panelCarrito.add(panelBotonesCarrito, BorderLayout.SOUTH);

        ventana.add(panelCatalogo);
        ventana.add(panelCarrito);

        // Colores de interfaz
        ventana.getContentPane().setBackground(new Color(245, 222, 179)); // fondo beige cálido
        panelCatalogo.setBackground(new Color(222, 184, 135)); // café claro
        panelCarrito.setBackground(new Color(222, 184, 135));

        listaCatalogo.setFont(new Font("Arial", Font.BOLD, 14));
        listaCatalogo.setBackground(new Color(255, 250, 240)); // crema
        listaCarrito.setFont(new Font("Arial", Font.PLAIN, 14));
        listaCarrito.setBackground(new Color(255, 250, 240));

        btnAgregar.setBackground(new Color(210, 180, 140)); // madera clara
        btnAgregar.setForeground(Color.BLACK);

        btnEliminar.setBackground(new Color(205, 133, 63)); // café medio
        btnEliminar.setForeground(Color.WHITE);

        btnFinalizar.setBackground(new Color(139, 69, 19)); // café oscuro
        btnFinalizar.setForeground(Color.WHITE);

        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        new tiendaApp();
    }
}
