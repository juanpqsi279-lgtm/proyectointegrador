package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.Comprador;
import com.tienda.model.Envio;
import com.tienda.model.ItemCarrito;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Panel de Perfil de Comprador (Patrón MVC - View).
 * Muestra los datos del cliente, gestión de dirección de entrega e historial completo de pedidos.
 */
public class PerfilCompradorPanel extends ScrollPane {

    private final TiendaController controller;
    private final TextField txtNombre;
    private final TextField txtEmail;
    private final TextField txtTelefono;
    private final TextArea txtDireccion;
    private final VBox containerHistorial;

    public PerfilCompradorPanel(TiendaController controller) {
        this.controller = controller;

        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(20));

        Comprador comprador = controller.getCompradorActual();

        // --- TARJETA 1: DATOS PERSONALES DEL COMPRADOR ---
        VBox cardPerfil = new VBox(16);
        cardPerfil.getStyleClass().add("section-card");

        HBox headerPerfil = new HBox(12);
        headerPerfil.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label("👤");
        avatar.setStyle("-fx-font-size: 32px; -fx-background-color: #F7EBE6; -fx-padding: 8 14 8 14; -fx-background-radius: 50%;");

        VBox titleBox = new VBox(2);
        Label lblTitulo = new Label("Perfil de Comprador");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");
        Label lblId = new Label("ID Cliente: " + comprador.getId());
        lblId.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");
        titleBox.getChildren().addAll(lblTitulo, lblId);

        headerPerfil.getChildren().addAll(avatar, titleBox);

        // Formulario de Datos
        GridPane gridForm = new GridPane();
        gridForm.setHgap(16);
        gridForm.setVgap(14);

        txtNombre = new TextField(comprador.getNombre());
        txtEmail = new TextField(comprador.getEmail());
        txtTelefono = new TextField(comprador.getTelefono());
        txtDireccion = new TextArea(comprador.getDireccion());
        txtDireccion.setPrefRowCount(3);
        txtDireccion.setWrapText(true);

        gridForm.add(new Label("Nombre Completo:"), 0, 0);
        gridForm.add(txtNombre, 1, 0);

        gridForm.add(new Label("Correo Electrónico:"), 0, 1);
        gridForm.add(txtEmail, 1, 1);

        gridForm.add(new Label("Teléfono de Contacto:"), 0, 2);
        gridForm.add(txtTelefono, 1, 2);

        gridForm.add(new Label("Dirección Predeterminada de Envío:"), 0, 3);
        gridForm.add(txtDireccion, 1, 3);

        Button btnGuardar = new Button("💾 Actualizar Dirección y Datos");
        btnGuardar.getStyleClass().add("btn-primary");
        btnGuardar.setOnAction(e -> {
            comprador.setNombre(txtNombre.getText());
            comprador.setEmail(txtEmail.getText());
            comprador.setTelefono(txtTelefono.getText());
            comprador.setDireccion(txtDireccion.getText());
            controller.solicitarCambioVista("NOTIFICAR_DATOS_ACTUALIZADOS");
        });

        cardPerfil.getChildren().addAll(headerPerfil, new Separator(), gridForm, btnGuardar);

        // --- TARJETA 2: HISTORIAL DE COMPRAS Y SEGUIMIENTO DE ENVÍOS ---
        VBox cardHistorial = new VBox(16);
        cardHistorial.getStyleClass().add("section-card");

        Label lblTituloHistorial = new Label("📦 Historial de Compras y Seguimiento de Envíos");
        lblTituloHistorial.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        containerHistorial = new VBox(14);
        renderizarHistorial();

        cardHistorial.getChildren().addAll(lblTituloHistorial, new Separator(), containerHistorial);

        mainContent.getChildren().addAll(cardPerfil, cardHistorial);
        this.setContent(mainContent);
    }

    /**
     * Renderiza las órdenes registradas en el historial del comprador.
     */
    public void renderizarHistorial() {
        containerHistorial.getChildren().clear();
        List<Comprador.OrdenCompra> ordenes = controller.getCompradorActual().getHistorialCompras();

        if (ordenes.isEmpty()) {
            Label empty = new Label("Aún no has realizado ninguna compra en el mercado artesanal.");
            empty.setStyle("-fx-text-fill: #6E6862; -fx-font-style: italic;");
            containerHistorial.getChildren().add(empty);
            return;
        }

        for (Comprador.OrdenCompra orden : ordenes) {
            VBox boxOrden = new VBox(10);
            boxOrden.setStyle("-fx-background-color: #F8F5F0; -fx-padding: 16px; -fx-background-radius: 10px; -fx-border-color: #E5DEC9; -fx-border-radius: 10px;");

            // Header de la Orden
            HBox headerOrd = new HBox(10);
            headerOrd.setAlignment(Pos.CENTER_LEFT);

            Label lblOrdenId = new Label("Orden " + orden.getIdOrden());
            lblOrdenId.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2C1E16;");

            Label lblFecha = new Label("• " + orden.getFecha());
            lblFecha.setStyle("-fx-text-fill: #6E6862; -fx-font-size: 12px;");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Envio.EstadoEnvio estadoEnvio = orden.getEnvio().getEstado();
            Label badgeEstado = new Label(estadoEnvio.getEtiqueta());
            badgeEstado.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 4 10 4 10; -fx-background-radius: 12px;",
                estadoEnvio.getColorHex()
            ));

            headerOrd.getChildren().addAll(lblOrdenId, lblFecha, sp, badgeEstado);

            // Detalle de Items
            VBox itemsBox = new VBox(4);
            for (ItemCarrito item : orden.getItems()) {
                Label lblItem = new Label("  • " + item.getCantidad() + "x " + item.getProducto().getNombre() + " (" + item.getSubtotalFormateado() + ")");
                lblItem.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C1E16;");
                itemsBox.getChildren().add(lblItem);
            }

            // Footer con Total y Dirección
            HBox footerOrd = new HBox(12);
            footerOrd.setAlignment(Pos.CENTER_LEFT);

            Label lblMetodo = new Label("Pago: " + orden.getMetodoPago().getTitulo());
            lblMetodo.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");

            Region sp2 = new Region();
            HBox.setHgrow(sp2, Priority.ALWAYS);

            Label lblTotal = new Label("Total: " + orden.getTotalFormateado());
            lblTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #C85A32;");

            footerOrd.getChildren().addAll(lblMetodo, sp2, lblTotal);

            boxOrden.getChildren().addAll(headerOrd, itemsBox, new Separator(), footerOrd);
            containerHistorial.getChildren().add(boxOrden);
        }
    }
}
