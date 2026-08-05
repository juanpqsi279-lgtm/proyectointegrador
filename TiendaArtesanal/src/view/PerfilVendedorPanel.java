package src.view;

import src.controller.tiendaController;
import src.model.Vendedor;
import src.model.producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

public class PerfilVendedorPanel extends JPanel {

    private NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    public PerfilVendedorPanel(tiendaController controller, Vendedor vendedor) {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);

        // Header del Vendedor
        JPanel header = createHeaderVendedor(vendedor);
        add(header, BorderLayout.NORTH);

        // Lista de Productos del Vendedor
        JPanel panelProductos = createProductosPanel(vendedor, controller);
        add(panelProductos, BorderLayout.CENTER);
    }

    private JPanel createHeaderVendedor(Vendedor v) {
        JPanel header = new JPanel(new BorderLayout(20, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tiendaApp.COLOR_CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(tiendaApp.COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Icono
        JLabel lblIcon = new JLabel(tiendaApp.getScaledIcon(v.getNombre().toLowerCase().contains("maría") ? "vendedor1" : "vendedor2"));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(tiendaApp.COLOR_BG);
        lblIcon.setBorder(BorderFactory.createLineBorder(tiendaApp.COLOR_BORDER, 2));

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel lblNombre = new JLabel(v.getNombre());
        lblNombre.setFont(new Font("Georgia", Font.BOLD, 26));
        lblNombre.setForeground(tiendaApp.COLOR_TEXT_DARK);
        
        JLabel lblDesc = new JLabel(v.getDescripcion());
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(tiendaApp.COLOR_TEXT_MUTED);

        JLabel lblCalificacion = new JLabel("Calificación: " + v.getCalificacion() + " / 5.0");
        lblCalificacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCalificacion.setForeground(new Color(255, 180, 0)); // Gold

        textPanel.add(lblNombre);
        textPanel.add(lblDesc);
        textPanel.add(lblCalificacion);

        header.add(lblIcon, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createProductosPanel(Vendedor v, tiendaController controller) {
        JPanel panel = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tiendaApp.COLOR_CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(tiendaApp.COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Productos de " + v.getNombre());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(tiendaApp.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        // Grid de productos
        JPanel grid = new JPanel(new GridLayout(0, 2, 15, 15));
        grid.setOpaque(false);

        for (producto p : v.getProductos()) {
            JPanel pCard = createProductCard(p, controller);
            grid.add(pCard);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProductCard(producto p, tiendaController controller) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tiendaApp.COLOR_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(tiendaApp.COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setOpaque(false);
        JLabel lblNombre = new JLabel(p.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblPrecio = new JLabel(formatoMoneda.format(p.getPrecio()));
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrecio.setForeground(tiendaApp.COLOR_PRIMARY);

        texts.add(lblNombre);
        texts.add(lblPrecio);
        
        card.add(texts, BorderLayout.CENTER);

        // Boton agregar
        JButton btnAdd = new JButton("+");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAdd.setBackground(tiendaApp.COLOR_PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(45, 45));
        btnAdd.addActionListener(e -> {
            controller.agregarProducto(p);
            JOptionPane.showMessageDialog(this, p.getNombre() + " agregado al carrito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.add(btnAdd);

        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }
}
