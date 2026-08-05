package src.view;

import src.model.producto;
import src.controller.tiendaController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class tiendaApp {
    private JFrame ventana;
    private DefaultListModel<producto> catalogoModel;
    private DefaultListModel<producto> carritoModel;
    private tiendaController controller;

    // Palette inspired by Figma artisanal warm design
    private static final Color COLOR_BG = new Color(249, 245, 240);       // Warm cream background
    private static final Color COLOR_CARD_BG = new Color(255, 255, 255);  // Card background
    private static final Color COLOR_PRIMARY = new Color(200, 109, 68);    // Terracotta Orange
    private static final Color COLOR_PRIMARY_HOVER = new Color(180, 90, 50);
    private static final Color COLOR_SECONDARY = new Color(125, 114, 108);  // Taupe / Muted brown
    private static final Color COLOR_SECONDARY_HOVER = new Color(100, 90, 85);
    private static final Color COLOR_TEXT_DARK = new Color(44, 34, 30);    // Deep espresso text
    private static final Color COLOR_TEXT_MUTED = new Color(138, 120, 110);// Muted brown text
    private static final Color COLOR_BORDER = new Color(234, 222, 212);    // Soft border
    private static final Color COLOR_SELECTED = new Color(245, 235, 225);  // Soft selection highlight
    private static final Color COLOR_GREEN_BADGE = new Color(88, 129, 87);  // Sage green accent

    private JLabel lblTotalCalculado;
    private JLabel lblContadorCarrito;
    private NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    public tiendaApp() {
        // Flat LookAndFeel if available or system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        controller = new tiendaController();

        ventana = new JFrame("Tienda Artesanal - Catálogo y Carrito");
        ventana.setSize(920, 620);
        ventana.setMinimumSize(new Dimension(800, 520));
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(COLOR_BG);
        mainContainer.setBorder(new EmptyBorder(15, 20, 20, 20));

        // Header Superior
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Modelos de lista
        catalogoModel = new DefaultListModel<>();
        catalogoModel.addElement(new producto("Pulsera tejida", 50));
        catalogoModel.addElement(new producto("Collar artesanal", 120));
        catalogoModel.addElement(new producto("Bolsa bordada", 300));
        catalogoModel.addElement(new producto("Taza artesanal", 200));
        catalogoModel.addElement(new producto("Cuadro pintado a mano", 500));
        catalogoModel.addElement(new producto("Jarrón de cerámica", 400));
        catalogoModel.addElement(new producto("Atrapasueños", 150));
        catalogoModel.addElement(new producto("Velas aromaticas artesanales", 250));

        carritoModel = new DefaultListModel<>();

        // Listas con Renderizador Personalizado Estilo Tarjeta
        JList<producto> listaCatalogo = new JList<>(catalogoModel);
        listaCatalogo.setCellRenderer(new ProductCardRenderer());
        listaCatalogo.setBackground(COLOR_BG);
        listaCatalogo.setSelectionBackground(COLOR_SELECTED);
        listaCatalogo.setFixedCellHeight(70);

        JList<producto> listaCarrito = new JList<>(carritoModel);
        listaCarrito.setCellRenderer(new ProductCardRenderer());
        listaCarrito.setBackground(COLOR_BG);
        listaCarrito.setSelectionBackground(COLOR_SELECTED);
        listaCarrito.setFixedCellHeight(70);

        // --- Panel Catálogo ---
        JPanel panelCatalogo = createCardPanel("Catálogo de Productos", catalogoModel.size() + " disponibles");
        JScrollPane scrollCatalogo = createStyledScrollPane(listaCatalogo);
        panelCatalogo.add(scrollCatalogo, BorderLayout.CENTER);

        JButton btnAgregar = new RoundedButton("+ Agregar al Carrito", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setPreferredSize(new Dimension(0, 42));
        btnAgregar.addActionListener(e -> {
            producto seleccionado = listaCatalogo.getSelectedValue();
            if (seleccionado != null) {
                controller.agregarProducto(seleccionado);
                carritoModel.addElement(seleccionado);
                actualizarResumenCarrito();
            } else {
                JOptionPane.showMessageDialog(ventana, "Por favor, selecciona un producto del catálogo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel panelBottomCatalogo = new JPanel(new BorderLayout());
        panelBottomCatalogo.setOpaque(false);
        panelBottomCatalogo.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelBottomCatalogo.add(btnAgregar, BorderLayout.CENTER);
        panelCatalogo.add(panelBottomCatalogo, BorderLayout.SOUTH);

        // --- Panel Carrito ---
        JPanel panelCarrito = createCardPanel("Tu Carrito", "0 ítems");
        // Guardar referencia al contador del carrito para actualizarlo
        Component headerComp = panelCarrito.getComponent(0);
        if (headerComp instanceof JPanel) {
            JPanel hp = (JPanel) headerComp;
            for (Component c : hp.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getForeground().equals(COLOR_GREEN_BADGE)) {
                    lblContadorCarrito = (JLabel) c;
                }
            }
        }

        JScrollPane scrollCarrito = createStyledScrollPane(listaCarrito);
        panelCarrito.add(scrollCarrito, BorderLayout.CENTER);

        // Panel de Resumen y Botones del Carrito
        JPanel panelResumenCarrito = new JPanel();
        panelResumenCarrito.setLayout(new BoxLayout(panelResumenCarrito, BoxLayout.Y_AXIS));
        panelResumenCarrito.setOpaque(false);
        panelResumenCarrito.setBorder(new EmptyBorder(12, 0, 0, 0));

        // Caja de Total
        JPanel cardTotal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SELECTED);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        cardTotal.setOpaque(false);
        cardTotal.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblTotalTexto = new JLabel("Total a pagar:");
        lblTotalTexto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalTexto.setForeground(COLOR_TEXT_DARK);

        lblTotalCalculado = new JLabel("$0.00");
        lblTotalCalculado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalCalculado.setForeground(COLOR_PRIMARY);

        cardTotal.add(lblTotalTexto, BorderLayout.WEST);
        cardTotal.add(lblTotalCalculado, BorderLayout.EAST);

        panelResumenCarrito.add(cardTotal);
        panelResumenCarrito.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botones de acción del Carrito
        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotonesCarrito.setOpaque(false);

        JButton btnEliminar = new RoundedButton("Eliminar", COLOR_SECONDARY, COLOR_SECONDARY_HOVER);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(0, 40));
        btnEliminar.addActionListener(e -> {
            producto seleccionado = listaCarrito.getSelectedValue();
            if (seleccionado != null) {
                controller.eliminarProducto(seleccionado);
                carritoModel.removeElement(seleccionado);
                actualizarResumenCarrito();
            } else {
                JOptionPane.showMessageDialog(ventana, "Selecciona un producto del carrito para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnFinalizar = new RoundedButton("Finalizar Compra", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFinalizar.setPreferredSize(new Dimension(0, 40));
        btnFinalizar.addActionListener(e -> {
            if (carritoModel.isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "El carrito está vacío.", "Carrito Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double total = controller.calcularTotal();
            JOptionPane.showMessageDialog(ventana, 
                "¡Gracias por tu compra en Tienda Artesanal!\n\nTotal pagado: " + formatoMoneda.format(total),
                "Compra Finalizada",
                JOptionPane.INFORMATION_MESSAGE);
            controller.limpiarCarrito();
            carritoModel.clear();
            actualizarResumenCarrito();
        });

        panelBotonesCarrito.add(btnEliminar);
        panelBotonesCarrito.add(btnFinalizar);
        panelResumenCarrito.add(panelBotonesCarrito);

        panelCarrito.add(panelResumenCarrito, BorderLayout.SOUTH);

        // Contenedor Central de 2 Columnas
        JPanel gridCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        gridCenter.setOpaque(false);
        gridCenter.add(panelCatalogo);
        gridCenter.add(panelCarrito);

        mainContainer.add(gridCenter, BorderLayout.CENTER);

        ventana.setContentPane(mainContainer);
        ventana.setVisible(true);
    }

    private void actualizarResumenCarrito() {
        double total = controller.calcularTotal();
        lblTotalCalculado.setText(formatoMoneda.format(total));
        if (lblContadorCarrito != null) {
            lblContadorCarrito.setText(carritoModel.size() + (carritoModel.size() == 1 ? " ítem" : " ítems"));
        }
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("Tienda Artesanal");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(COLOR_TEXT_DARK);

        JLabel subtitle = new JLabel("Catálogo de piezas hechas a mano y artesanías únicas");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(COLOR_TEXT_MUTED);

        JPanel textGroup = new JPanel(new GridLayout(2, 1, 0, 2));
        textGroup.setOpaque(false);
        textGroup.add(title);
        textGroup.add(subtitle);

        JLabel badgeHeader = new JLabel("\u25CF EDICI\u00D3N ARTESANAL");
        badgeHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badgeHeader.setForeground(COLOR_PRIMARY);

        header.add(textGroup, BorderLayout.WEST);
        header.add(badgeHeader, BorderLayout.EAST);

        return header;
    }

    private JPanel createCardPanel(String titulo, String badgeText) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(COLOR_TEXT_DARK);

        JLabel lblBadge = new JLabel(badgeText);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setForeground(COLOR_GREEN_BADGE);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblBadge, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        return card;
    }

    private JScrollPane createStyledScrollPane(JComponent view) {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        return scroll;
    }

    // --- Renderizador Estilo Tarjeta para los Productos ---
    private static class ProductCardRenderer implements ListCellRenderer<producto> {
        private static final Map<String, ImageIcon> iconCache = new HashMap<>();
        private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        @Override
        public Component getListCellRendererComponent(JList<? extends producto> list, producto p, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel card = new JPanel(new BorderLayout(12, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (isSelected) {
                        g2.setColor(COLOR_SELECTED);
                        g2.fill(new RoundRectangle2D.Float(0, 2, getWidth(), getHeight() - 4, 12, 12));
                        g2.setColor(COLOR_PRIMARY);
                        g2.fill(new RoundRectangle2D.Float(0, 6, 4, getHeight() - 12, 4, 4)); // Barra lateral seleccionada
                    } else {
                        g2.setColor(COLOR_BG);
                        g2.fill(new RoundRectangle2D.Float(0, 2, getWidth(), getHeight() - 4, 12, 12));
                        g2.setColor(COLOR_BORDER);
                        g2.draw(new RoundRectangle2D.Float(0, 2, getWidth() - 1, getHeight() - 5, 12, 12));
                    }
                    g2.dispose();
                }
            };
            card.setOpaque(false);
            card.setBorder(new EmptyBorder(6, 12, 6, 12));

            // Icono
            String nombre = p != null ? p.getNombre().toLowerCase() : "";
            ImageIcon icon = getScaledIcon(nombre);
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setPreferredSize(new Dimension(48, 48));

            // Textos
            JLabel lblNombre = new JLabel(p != null ? p.getNombre() : "");
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNombre.setForeground(COLOR_TEXT_DARK);

            JLabel lblSub = new JLabel("Artesanal · Hecho a mano");
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(COLOR_TEXT_MUTED);

            JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            textPanel.setOpaque(false);
            textPanel.add(lblNombre);
            textPanel.add(lblSub);

            // Precio Badge
            JLabel lblPrecio = new JLabel(p != null ? currencyFormat.format(p.getPrecio()) : "");
            lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblPrecio.setForeground(COLOR_PRIMARY);
            lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);

            card.add(lblIcon, BorderLayout.WEST);
            card.add(textPanel, BorderLayout.CENTER);
            card.add(lblPrecio, BorderLayout.EAST);

            return card;
        }

        private ImageIcon getScaledIcon(String nombre) {
            if (iconCache.containsKey(nombre)) {
                return iconCache.get(nombre);
            }

            String path = "src/images/pulsera.png";
            if (nombre.contains("pulsera")) {
                path = "src/images/pulsera.png";
            } else if (nombre.contains("collar")) {
                path = "src/images/collar.png";
            } else if (nombre.contains("bolsa")) {
                path = "src/images/bolsa.png";
            } else if (nombre.contains("taza")) {
                path = "src/images/taza.png";
            } else if (nombre.contains("cuadro")) {
                path = "src/images/cuadro.jpg";
            } else if (nombre.contains("jarrón")) {
                path = "src/images/jarron.png";
            } else if (nombre.contains("atrapasueños")) {
                path = "src/images/atrapasueños.png";
            } else if (nombre.contains("vela")) {
                path = "src/images/Velas.png";
            }

            ImageIcon original = new ImageIcon(path);
            if (original.getImageLoadStatus() == MediaTracker.COMPLETE && original.getIconWidth() > 0) {
                Image img = original.getImage();
                // Escalado suave con Anti-Aliasing
                Image scaled = img.getScaledInstance(44, 44, Image.SCALE_SMOOTH);
                ImageIcon result = new ImageIcon(scaled);
                iconCache.put(nombre, result);
                return result;
            }

            // Fallback en caso de que no cargue la imagen
            ImageIcon defaultIcon = new ImageIcon(new java.awt.image.BufferedImage(44, 44, java.awt.image.BufferedImage.TYPE_INT_ARGB));
            iconCache.put(nombre, defaultIcon);
            return defaultIcon;
        }
    }

    // --- Botón Personalizado Redondeado con Efecto Hover ---
    private static class RoundedButton extends JButton {
        private Color bgColor;
        private Color hoverColor;
        private boolean isHovered = false;

        public RoundedButton(String text, Color bg, Color hover) {
            super(text);
            this.bgColor = bg;
            this.hoverColor = hover;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isHovered ? hoverColor : bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new tiendaApp());
    }
}
