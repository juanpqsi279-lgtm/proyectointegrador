package src.view;

import src.controller.tiendaController;
import src.model.Comprador;
import src.model.Envio;
import src.model.MetodoPago;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PerfilCompradorPanel extends JPanel {

    public PerfilCompradorPanel(tiendaController controller) {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        
        Comprador comprador = controller.getCompradorActual();

        // Header Perfil
        JPanel header = createHeaderPerfil(comprador);
        add(header, BorderLayout.NORTH);

        // Centro: 2 Columnas (Envios y Métodos de Pago)
        JPanel gridCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        gridCenter.setOpaque(false);

        JPanel panelEnvios = createEnviosPanel(comprador);
        JPanel panelPagos = createPagosPanel(comprador);

        gridCenter.add(panelEnvios);
        gridCenter.add(panelPagos);

        add(gridCenter, BorderLayout.CENTER);
    }

    private JPanel createHeaderPerfil(Comprador c) {
        JPanel header = new JPanel(new BorderLayout(15, 15)) {
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
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Placeholder para imagen de perfil
        JLabel lblIcon = new JLabel(" AG ", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblIcon.setForeground(tiendaApp.COLOR_PRIMARY);
        lblIcon.setOpaque(true);
        lblIcon.setBackground(tiendaApp.COLOR_SELECTED);
        lblIcon.setBorder(BorderFactory.createLineBorder(tiendaApp.COLOR_BORDER, 2));

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setOpaque(false);
        
        JLabel lblNombre = new JLabel("Hola, " + c.getNombre());
        lblNombre.setFont(new Font("Georgia", Font.BOLD, 22));
        lblNombre.setForeground(tiendaApp.COLOR_TEXT_DARK);
        
        JLabel lblEmail = new JLabel(c.getEmail());
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setForeground(tiendaApp.COLOR_TEXT_MUTED);

        JLabel lblDir = new JLabel("Dirección: " + c.getDireccion());
        lblDir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDir.setForeground(tiendaApp.COLOR_SECONDARY);

        textPanel.add(lblNombre);
        textPanel.add(lblEmail);
        textPanel.add(lblDir);

        header.add(lblIcon, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createEnviosPanel(Comprador c) {
        JPanel panel = new JPanel(new BorderLayout(0, 10)) {
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
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Seguimiento de Envíos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(tiendaApp.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (Envio e : c.getEnvios()) {
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setOpaque(false);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, tiendaApp.COLOR_BORDER),
                new EmptyBorder(10, 5, 10, 5)
            ));

            JPanel texts = new JPanel(new GridLayout(3, 1));
            texts.setOpaque(false);
            
            JLabel lblGuia = new JLabel("Guía: " + e.getNumeroGuia());
            lblGuia.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblGuia.setForeground(tiendaApp.COLOR_TEXT_DARK);
            
            // Construir texto de descripcion con los nombres de productos
            StringBuilder descBuilder = new StringBuilder();
            for (int i = 0; i < e.getProductos().size(); i++) {
                descBuilder.append(e.getProductos().get(i).getNombre());
                if (i < e.getProductos().size() - 1) descBuilder.append(" + ");
            }
            JLabel lblDesc = new JLabel(descBuilder.toString());
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesc.setForeground(tiendaApp.COLOR_TEXT_MUTED);
            
            JLabel lblFecha = new JLabel("Entrega: " + e.getFechaEstimada());
            lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblFecha.setForeground(tiendaApp.COLOR_SECONDARY);

            texts.add(lblGuia);
            texts.add(lblDesc);
            texts.add(lblFecha);
            
            // Panel para las imagenes de los productos
            JPanel imagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            imagesPanel.setOpaque(false);
            for (src.model.producto p : e.getProductos()) {
                JLabel lblImg = new JLabel(tiendaApp.getScaledIcon(p.getNombre()));
                imagesPanel.add(lblImg);
            }
            
            JPanel centerContent = new JPanel(new BorderLayout());
            centerContent.setOpaque(false);
            centerContent.add(texts, BorderLayout.CENTER);
            centerContent.add(imagesPanel, BorderLayout.SOUTH);

            JLabel lblEstado = new JLabel(e.getEstado());
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
            if (e.getEstado().equals("Entregado")) {
                lblEstado.setForeground(tiendaApp.COLOR_GREEN_BADGE);
            } else {
                lblEstado.setForeground(tiendaApp.COLOR_PRIMARY);
            }

            card.add(centerContent, BorderLayout.CENTER);
            card.add(lblEstado, BorderLayout.EAST);
            list.add(card);
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPagosPanel(Comprador c) {
        JPanel panel = new JPanel(new BorderLayout(0, 10)) {
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
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Métodos de Pago");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(tiendaApp.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        final JPanel[] selectedCard = new JPanel[1];

        for (MetodoPago m : c.getMetodosPago()) {
            JPanel card = new JPanel(new BorderLayout(10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    if (isOpaque()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getBackground());
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        g2.dispose();
                    }
                }
            };
            card.setOpaque(false);
            card.setBackground(tiendaApp.COLOR_SELECTED);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, tiendaApp.COLOR_BORDER),
                new EmptyBorder(10, 10, 10, 10)
            ));

            JLabel lblIcon = new JLabel(" CC ", SwingConstants.CENTER);
            lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblIcon.setForeground(tiendaApp.COLOR_PRIMARY);
            lblIcon.setOpaque(true);
            lblIcon.setBackground(tiendaApp.COLOR_SELECTED);
            lblIcon.setBorder(BorderFactory.createLineBorder(tiendaApp.COLOR_BORDER));

            JPanel texts = new JPanel(new GridLayout(2, 1));
            texts.setOpaque(false);
            
            JLabel lblTipo = new JLabel(m.getTipo());
            lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTipo.setForeground(tiendaApp.COLOR_TEXT_DARK);
            
            JLabel lblInfo = new JLabel("Termina en " + m.getUltimosDigitos());
            lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblInfo.setForeground(tiendaApp.COLOR_TEXT_MUTED);
            
            texts.add(lblTipo);
            texts.add(lblInfo);

            card.add(lblIcon, BorderLayout.WEST);
            card.add(texts, BorderLayout.CENTER);
            
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (selectedCard[0] != null) {
                        selectedCard[0].setOpaque(false);
                        selectedCard[0].repaint();
                    }
                    selectedCard[0] = card;
                    card.setOpaque(true);
                    card.repaint();
                }
            });

            list.add(card);
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}
