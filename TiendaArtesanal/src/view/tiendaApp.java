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
        catalogoModel.addElement(new producto("Taza artesanal", 200));
        catalogoModel.addElement(new producto("Cuadro pintado a mano", 500));
        catalogoModel.addElement(new producto("Jarrón de cerámica", 400));
        catalogoModel.addElement(new producto("Atrapasueños", 150));
        catalogoModel.addElement(new producto("Velas aromaticas artesanales", 250));
        JList<producto> listaCatalogo = new JList<>(catalogoModel);

        // Mostrar imagen junto al nombre del producto
listaCatalogo.setCellRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        producto p = (producto) value;

        // Asignar imagen según producto
        String nombre = p.getNombre().toLowerCase();
        ImageIcon icon = null;

        if (nombre.contains("pulsera")) {
            icon = new ImageIcon("src/images/pulsera.png");
        } else if (nombre.contains("collar")) {
            icon = new ImageIcon("src/images/collar.png");
        } else if (nombre.contains("bolsa")) {
            icon = new ImageIcon("src/images/bolsa.png");
        } else if (nombre.contains("taza")) {
            icon = new ImageIcon("src/images/taza.png");
        } else if (nombre.contains("cuadro")) {
            icon = new ImageIcon("src/images/cuadro.jpg");
        } else if (nombre.contains("jarrón")) {
            icon = new ImageIcon("src/images/jarron.png");
        } else if (nombre.contains("atrapasueños")) {
            icon = new ImageIcon("src/images/atrapasueños.png");
        } else if (nombre.contains("vela")) {
            icon = new ImageIcon("src/images/Velas.png");
        }

        // Escalar imagen para que se vea uniforme
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }

        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        return label;
    }
});


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
