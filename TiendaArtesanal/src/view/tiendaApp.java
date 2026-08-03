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
        panelCatalogo.add(new JLabel("Catálogo"), BorderLayout.NORTH);
        panelCatalogo.add(new JScrollPane(listaCatalogo), BorderLayout.CENTER);
        panelCatalogo.add(btnAgregar, BorderLayout.SOUTH);

        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.add(new JLabel("Carrito"), BorderLayout.NORTH);
        panelCarrito.add(new JScrollPane(listaCarrito), BorderLayout.CENTER);

        // Panel de botones del carrito
        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 2));
        panelBotonesCarrito.add(btnEliminar);
        panelBotonesCarrito.add(btnFinalizar);

        panelCarrito.add(panelBotonesCarrito, BorderLayout.SOUTH);

        ventana.add(panelCatalogo);
        ventana.add(panelCarrito);

        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        new tiendaApp();
    }
}
