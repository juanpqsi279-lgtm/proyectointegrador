package src.view;

import src.model.producto;
import src.model.Vendedor;
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
    private tiendaController controller;

    // Colores Públicos para que los paneles hijos los usen
    public static final Color COLOR_BG = new Color(249, 245, 240);
    public static final Color COLOR_CARD_BG = new Color(255, 255, 255);
    public static final Color COLOR_PRIMARY = new Color(200, 109, 68);
    public static final Color COLOR_PRIMARY_HOVER = new Color(180, 90, 50);
    public static final Color COLOR_SECONDARY = new Color(125, 114, 108);
    public static final Color COLOR_SECONDARY_HOVER = new Color(100, 90, 85);
    public static final Color COLOR_TEXT_DARK = new Color(44, 34, 30);
    public static final Color COLOR_TEXT_MUTED = new Color(138, 120, 110);
    public static final Color COLOR_BORDER = new Color(234, 222, 212);
    public static final Color COLOR_SELECTED = new Color(245, 235, 225);
    public static final Color COLOR_GREEN_BADGE = new Color(88, 129, 87);

    private NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    // Elementos de la Tienda Original
    private DefaultListModel<producto> catalogoModel;
    private DefaultListModel<producto> carritoModel;
    private JLabel lblTotalCalculado;
    private JLabel lblContadorCarrito;
    private JList<producto> listaCarrito;

    // CardLayout
    private JPanel cards;
    private CardLayout cardLayout;

    public tiendaApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        controller = new tiendaController();

        ventana = new JFrame("Tienda Artesanal");
        ventana.setSize(1000, 700);
        ventana.setMinimumSize(new Dimension(900, 600));
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(COLOR_BG);

        // --- SIDEBAR NAVEGACIÓN ---
        JPanel sidebar = createSidebar();
        mainContainer.add(sidebar, BorderLayout.WEST);

        // --- CARD LAYOUT (CONTENIDO) ---
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Panel Tienda (Catálogo)
        JPanel panelTienda = createTiendaPanel();
        cards.add(panelTienda, "Tienda");
        
        // 1.5 Panel Carrito
        JPanel panelCarrito = createCarritoPanel();
        cards.add(panelCarrito, "Carrito");

        // 2. Panel Mi Perfil (Comprador)
        PerfilCompradorPanel panelPerfil = new PerfilCompradorPanel(controller);
        cards.add(panelPerfil, "MiPerfil");

        // 3. Panel Vendedores
        JPanel panelVendedores = createVendedoresContainer();
        cards.add(panelVendedores, "Vendedores");

        mainContainer.add(cards, BorderLayout.CENTER);

        ventana.setContentPane(mainContainer);
        ventana.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_CARD_BG);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Logo / Título
        JLabel title = new JLabel("Tienda");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(COLOR_TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Artesanal");
        subtitle.setFont(new Font("Georgia", Font.ITALIC, 18));
        subtitle.setForeground(COLOR_PRIMARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        sidebar.add(title);
        sidebar.add(subtitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Botones de Navegación
        sidebar.add(createNavButton("Catálogo", "Tienda"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createNavButton("Carrito", "Carrito"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createNavButton("Mi Perfil", "MiPerfil"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createNavButton("Vendedores", "Vendedores"));

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXT_DARK);
        btn.setBackground(COLOR_CARD_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SELECTED); }
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD_BG); }
        });

        btn.addActionListener(e -> cardLayout.show(cards, cardName));
        return btn;
    }

    private JPanel createVendedoresContainer() {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);

        // Selector de vendedor
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JLabel lbl = new JLabel("Seleccionar Vendedor: ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JComboBox<String> combo = new JComboBox<>();
        for (Vendedor v : controller.getVendedores()) {
            combo.addItem(v.getNombre());
        }

        top.add(lbl);
        top.add(combo);
        container.add(top, BorderLayout.NORTH);

        // CardLayout para los perfiles de vendedores
        CardLayout vLayout = new CardLayout();
        JPanel vCards = new JPanel(vLayout);
        vCards.setOpaque(false);

        for (Vendedor v : controller.getVendedores()) {
            vCards.add(new PerfilVendedorPanel(controller, v), v.getNombre());
        }

        combo.addActionListener(e -> {
            String selected = (String) combo.getSelectedItem();
            vLayout.show(vCards, selected);
        });

        container.add(vCards, BorderLayout.CENTER);
        return container;
    }

    // --- CÓDIGO ORIGINAL DE LA TIENDA ADAPTADO ---

    private JPanel createTiendaPanel() {
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setOpaque(false);

        catalogoModel = new DefaultListModel<>();
        for (Vendedor v : controller.getVendedores()) {
            for (producto p : v.getProductos()) {
                catalogoModel.addElement(p);
            }
        }

        JList<producto> listaCatalogo = new JList<>(catalogoModel);
        listaCatalogo.setCellRenderer(new ProductCardRenderer());
        listaCatalogo.setBackground(COLOR_BG);
        listaCatalogo.setSelectionBackground(COLOR_SELECTED);
        listaCatalogo.setFixedCellHeight(70);

        JPanel panelCatalogo = createCardPanel("Catálogo", catalogoModel.size() + " disponibles");
        JScrollPane scrollCatalogo = createStyledScrollPane(listaCatalogo);
        panelCatalogo.add(scrollCatalogo, BorderLayout.CENTER);

        JButton btnAgregar = new RoundedButton("+ Agregar al Carrito", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setPreferredSize(new Dimension(0, 42));
        btnAgregar.addActionListener(e -> {
            producto seleccionado = listaCatalogo.getSelectedValue();
            if (seleccionado != null) {
                controller.agregarProducto(seleccionado);
                JOptionPane.showMessageDialog(ventana, seleccionado.getNombre() + " agregado al carrito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ventana, "Selecciona un producto.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JPanel panelBottomCatalogo = new JPanel(new BorderLayout());
        panelBottomCatalogo.setOpaque(false);
        panelBottomCatalogo.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelBottomCatalogo.add(btnAgregar, BorderLayout.CENTER);
        panelCatalogo.add(panelBottomCatalogo, BorderLayout.SOUTH);

        mainContainer.add(panelCatalogo, BorderLayout.CENTER);
        return mainContainer;
    }
    
    private JPanel createCarritoPanel() {
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setOpaque(false);
        
        carritoModel = new DefaultListModel<>();
        listaCarrito = new JList<>(carritoModel);
        listaCarrito.setCellRenderer(new ProductCardRenderer());
        listaCarrito.setBackground(COLOR_BG);
        listaCarrito.setSelectionBackground(COLOR_SELECTED);
        listaCarrito.setFixedCellHeight(70);

        JPanel panelCarrito = createCardPanel("Tu Carrito", "0 ítems");
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

        JPanel panelResumenCarrito = new JPanel();
        panelResumenCarrito.setLayout(new BoxLayout(panelResumenCarrito, BoxLayout.Y_AXIS));
        panelResumenCarrito.setOpaque(false);
        panelResumenCarrito.setBorder(new EmptyBorder(12, 0, 0, 0));

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
        
        lblTotalCalculado = new JLabel("$0.00");
        lblTotalCalculado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalCalculado.setForeground(COLOR_PRIMARY);
        
        cardTotal.add(lblTotalTexto, BorderLayout.WEST);
        cardTotal.add(lblTotalCalculado, BorderLayout.EAST);
        panelResumenCarrito.add(cardTotal);
        panelResumenCarrito.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotonesCarrito.setOpaque(false);

        JButton btnEliminar = new RoundedButton("Eliminar", COLOR_SECONDARY, COLOR_SECONDARY_HOVER);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(0, 40));
        btnEliminar.addActionListener(e -> {
            producto seleccionado = listaCarrito.getSelectedValue();
            if (seleccionado != null) {
                controller.eliminarProducto(seleccionado);
                refrescarCarritoUI();
            }
        });

        JButton btnFinalizar = new RoundedButton("Finalizar Compra", COLOR_PRIMARY, COLOR_PRIMARY_HOVER);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFinalizar.setPreferredSize(new Dimension(0, 40));
        btnFinalizar.addActionListener(e -> {
            if (controller.getCarrito().isEmpty()) return;
            double total = controller.calcularTotal();
            JOptionPane.showMessageDialog(ventana, "Compra Finalizada\nTotal: " + formatoMoneda.format(total));
            controller.limpiarCarrito();
            refrescarCarritoUI();
        });

        panelBotonesCarrito.add(btnEliminar);
        panelBotonesCarrito.add(btnFinalizar);
        panelResumenCarrito.add(panelBotonesCarrito);
        panelCarrito.add(panelResumenCarrito, BorderLayout.SOUTH);

        mainContainer.add(panelCarrito, BorderLayout.CENTER);
        
        mainContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refrescarCarritoUI();
            }
        });

        return mainContainer;
    }

    public void refrescarCarritoUI() {
        if (carritoModel != null) {
            carritoModel.clear();
            for (producto p : controller.getCarrito()) {
                carritoModel.addElement(p);
            }
        }
        double total = controller.calcularTotal();
        if (lblTotalCalculado != null) {
            lblTotalCalculado.setText(formatoMoneda.format(total));
        }
        if (lblContadorCarrito != null) {
            lblContadorCarrito.setText(controller.getCarrito().size() + " ítems");
        }
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

    public static ImageIcon getScaledIcon(String nombre) {
        String base = nombre.toLowerCase().split(" ")[0];
        String[] extensions = {".png", ".jpg", ".jpeg"};
        ImageIcon original = null;
        for (String ext : extensions) {
            String path = "src/images/" + base + ext;
            if (new java.io.File(path).exists()) {
                original = new ImageIcon(path);
                break;
            }
        }
        
        if (original != null && original.getImageLoadStatus() == MediaTracker.COMPLETE && original.getIconWidth() > 0) {
            Image img = original.getImage();
            Image scaled = img.getScaledInstance(44, 44, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        
        // Fallback dinámico si no existe la imagen
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(44, 44, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(COLOR_SELECTED);
        g2.fillRoundRect(0, 0, 44, 44, 12, 12);
        g2.setColor(COLOR_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        String init = nombre.isEmpty() ? "P" : nombre.substring(0, 1).toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(init, (44 - fm.stringWidth(init)) / 2, ((44 - fm.getHeight()) / 2) + fm.getAscent());
        g2.dispose();
        return new ImageIcon(img);
    }

    public static class ProductCardRenderer implements ListCellRenderer<producto> {
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
                        g2.fill(new RoundRectangle2D.Float(0, 6, 4, getHeight() - 12, 4, 4));
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
            
            JLabel lblIcon = new JLabel();
            if (p != null) lblIcon.setIcon(getScaledIcon(p.getNombre()));
            
            JLabel lblNombre = new JLabel(p != null ? p.getNombre() : "");
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel lblPrecio = new JLabel(p != null ? currencyFormat.format(p.getPrecio()) : "");
            lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblPrecio.setForeground(COLOR_PRIMARY);
            
            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setOpaque(false);
            textPanel.add(lblNombre, BorderLayout.CENTER);

            card.add(lblIcon, BorderLayout.WEST);
            card.add(textPanel, BorderLayout.CENTER);
            card.add(lblPrecio, BorderLayout.EAST);
            return card;
        }
    }

    public static class RoundedButton extends JButton {
        private Color bgColor, hoverColor;
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
                public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
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
