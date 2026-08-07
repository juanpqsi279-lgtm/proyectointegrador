package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.ItemCarrito;
import com.tienda.model.MetodoPago;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Panel de Carrito de Compras y Checkout (Patrón MVC - View).
 * Permite revisar los artículos seleccionados, modificar cantidades,
 * elegir método de pago (Tarjeta, Transferencia, Efectivo) y confirmar la compra.
 */
public class CarritoCheckoutPanel extends ScrollPane {

    private final TiendaController controller;
    private final VBox containerItems;
    private final Label lblSubtotalVal;
    private final Label lblEnvioVal;
    private final Label lblTotalVal;
    private final TextArea txtDireccionEnvio;
    private final ToggleGroup grupoPago;
    private final Label lblDetallePago;

    public CarritoCheckoutPanel(TiendaController controller) {
        this.controller = controller;

        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        HBox layoutPrincipal = new HBox(24);
        layoutPrincipal.setPadding(new Insets(20));

        // ==========================================
        // COLUMNA IZQUIERDA: LISTA DE ARTÍCULOS EN CARRITO
        // ==========================================
        VBox colIzquierda = new VBox(16);
        colIzquierda.getStyleClass().add("section-card");
        HBox.setHgrow(colIzquierda, Priority.ALWAYS);

        HBox headerCarrito = new HBox(12);
        headerCarrito.setAlignment(Pos.CENTER_LEFT);

        Label lblTituloCarrito = new Label("🛒 Artículos en Tu Carrito");
        lblTituloCarrito.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        Region spHeader = new Region();
        HBox.setHgrow(spHeader, Priority.ALWAYS);

        Button btnVaciar = new Button("🗑️ Vaciar Carrito");
        btnVaciar.getStyleClass().add("btn-secondary");
        btnVaciar.setOnAction(e -> controller.vaciarCarrito());

        headerCarrito.getChildren().addAll(lblTituloCarrito, spHeader, btnVaciar);

        containerItems = new VBox(12);

        colIzquierda.getChildren().addAll(headerCarrito, new Separator(), containerItems);

        // ==========================================
        // COLUMNA DERECHA: RESUMEN DE COMPRA Y CHECKOUT
        // ==========================================
        VBox colDerecha = new VBox(20);
        colDerecha.getStyleClass().add("section-card");
        colDerecha.setPrefWidth(380);
        colDerecha.setMinWidth(340);

        Label lblTituloCheckout = new Label("📋 Resumen de Pago y Envío");
        lblTituloCheckout.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        // --- Desglose Financiero ---
        VBox boxPrecios = new VBox(10);
        boxPrecios.setStyle("-fx-background-color: #F8F5F0; -fx-padding: 16px; -fx-background-radius: 10px;");

        lblSubtotalVal = new Label("$0.00 MXN");
        lblSubtotalVal.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        lblEnvioVal = new Label("$0.00 MXN");
        lblEnvioVal.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        lblTotalVal = new Label("$0.00 MXN");
        lblTotalVal.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #C85A32;");

        boxPrecios.getChildren().addAll(
            crearFilaResumen("Subtotal Productos:", lblSubtotalVal),
            crearFilaResumen("Costo Envío Artesanal:", lblEnvioVal),
            new Separator(),
            crearFilaResumen("TOTAL A PAGAR:", lblTotalVal)
        );

        // --- Dirección de Envío ---
        VBox boxDireccion = new VBox(8);
        Label lblDirTitle = new Label("📍 Dirección Destino:");
        lblDirTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        txtDireccionEnvio = new TextArea(controller.getCompradorActual().getDireccion());
        txtDireccionEnvio.setPrefRowCount(3);
        txtDireccionEnvio.setWrapText(true);
        boxDireccion.getChildren().addAll(lblDirTitle, txtDireccionEnvio);

        // --- Selección de Método de Pago ---
        VBox boxPago = new VBox(10);
        Label lblPagoTitle = new Label("💳 Selección de Método de Pago:");
        lblPagoTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        grupoPago = new ToggleGroup();
        VBox opcionesPagoBox = new VBox(8);

        lblDetallePago = new Label();
        lblDetallePago.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862; -fx-font-style: italic; -fx-background-color: #F0EAE1; -fx-padding: 8px; -fx-background-radius: 6px;");
        lblDetallePago.setWrapText(true);

        for (MetodoPago mp : MetodoPago.values()) {
            RadioButton rb = new RadioButton(mp.getTitulo());
            rb.setStyle("-fx-text-fill: #2C1E16; -fx-font-size: 14px;");
            rb.setToggleGroup(grupoPago);
            rb.setUserData(mp);

            if (mp == MetodoPago.TARJETA) {
                rb.setSelected(true);
                lblDetallePago.setText(mp.getDescripcion());
            }

            rb.setOnAction(e -> lblDetallePago.setText(mp.getDescripcion()));
            opcionesPagoBox.getChildren().add(rb);
        }

        boxPago.getChildren().addAll(lblPagoTitle, opcionesPagoBox, lblDetallePago);

        // --- Botón de Confirmación ---
        Button btnConfirmar = new Button("🔒 Confirmar y Realizar Pedido");
        btnConfirmar.getStyleClass().add("btn-success");
        btnConfirmar.setMaxWidth(Double.MAX_VALUE);
        btnConfirmar.setPrefHeight(45);

        btnConfirmar.setOnAction(e -> {
            Toggle selected = grupoPago.getSelectedToggle();
            if (selected != null) {
                MetodoPago mp = (MetodoPago) selected.getUserData();
                boolean completado = controller.procesarCompra(txtDireccionEnvio.getText(), mp);
                if (completado) {
                    controller.solicitarCambioVista("Navegar_Historial");
                }
            }
        });

        colDerecha.getChildren().addAll(
            lblTituloCheckout,
            new Separator(),
            boxPrecios,
            boxDireccion,
            boxPago,
            btnConfirmar
        );

        layoutPrincipal.getChildren().addAll(colIzquierda, colDerecha);
        this.setContent(layoutPrincipal);

        // Renderizado inicial y escucha de cambios
        renderizarCarrito();
        controller.getCarritoCompras().addListener((ListChangeListener.Change<? extends ItemCarrito> c) -> renderizarCarrito());
    }

    private HBox crearFilaResumen(String etiqueta, Label lblValor) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(etiqueta);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C1E16;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        row.getChildren().addAll(lbl, sp, lblValor);
        return row;
    }

    /**
     * Reconstruye la lista de elementos en el carrito y recalcula totales.
     */
    public void renderizarCarrito() {
        containerItems.getChildren().clear();

        if (controller.getCarritoCompras().isEmpty()) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));

            Label iconEmpty = new Label("🛒");
            iconEmpty.setStyle("-fx-font-size: 42px;");

            Label lblMsg = new Label("Tu carrito de compras está vacío.");
            lblMsg.setStyle("-fx-font-size: 15px; -fx-text-fill: #6E6862; -fx-font-weight: bold;");

            Button btnIrCatalogo = new Button("Ver Catálogo de Artesanías");
            btnIrCatalogo.getStyleClass().add("btn-primary");
            btnIrCatalogo.setOnAction(e -> controller.solicitarCambioVista("Ir_Catalogo"));

            emptyBox.getChildren().addAll(iconEmpty, lblMsg, btnIrCatalogo);
            containerItems.getChildren().add(emptyBox);
        } else {
            for (ItemCarrito item : controller.getCarritoCompras()) {
                HBox itemCard = crearFilaItemCarrito(item);
                containerItems.getChildren().add(itemCard);
            }
        }

        // Actualizar totales financieros
        lblSubtotalVal.setText(String.format("$%.2f MXN", controller.calcularSubtotalCarrito()));
        lblEnvioVal.setText(String.format("$%.2f MXN", controller.calcularCostoEnvio()));
        lblTotalVal.setText(String.format("$%.2f MXN", controller.calcularTotalCarrito()));
    }

    private HBox crearFilaItemCarrito(ItemCarrito item) {
        HBox card = new HBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #F8F5F0; -fx-padding: 12px; -fx-background-radius: 10px; -fx-border-color: #E5DEC9; -fx-border-radius: 10px;");

        VBox infoBox = new VBox(4);
        Label lblNombre = new Label(item.getProducto().getNombre());
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2C1E16;");

        Label lblArtesano = new Label("Por " + item.getProducto().getArtesanoNombre() + " • " + item.getProducto().obtenerPrecioFormateado() + " c/u");
        lblArtesano.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");

        infoBox.getChildren().addAll(lblNombre, lblArtesano);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Control de Cantidad (+ / -)
        HBox contCant = new HBox(6);
        contCant.setAlignment(Pos.CENTER);

        Button btnMenos = new Button("-");
        btnMenos.getStyleClass().add("btn-secondary");
        btnMenos.setPrefSize(30, 30);
        btnMenos.setOnAction(e -> controller.modificarCantidadCarrito(item, item.getCantidad() - 1));

        Label lblCant = new Label(String.valueOf(item.getCantidad()));
        lblCant.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 0 6 0 6;");

        Button btnMas = new Button("+");
        btnMas.getStyleClass().add("btn-secondary");
        btnMas.setPrefSize(30, 30);
        btnMas.setOnAction(e -> controller.modificarCantidadCarrito(item, item.getCantidad() + 1));

        contCant.getChildren().addAll(btnMenos, lblCant, btnMas);

        // Subtotal por item
        Label lblSubtotal = new Label(item.getSubtotalFormateado());
        lblSubtotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #C85A32; -fx-pref-width: 90px; -fx-alignment: CENTER_RIGHT;");

        // Botón Eliminar
        Button btnEliminar = new Button("❌");
        btnEliminar.getStyleClass().add("btn-secondary");
        btnEliminar.setTooltip(new Tooltip("Eliminar del carrito"));
        btnEliminar.setOnAction(e -> controller.eliminarDelCarrito(item));

        card.getChildren().addAll(infoBox, contCant, lblSubtotal, btnEliminar);
        return card;
    }
}
