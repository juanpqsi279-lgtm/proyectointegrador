package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.Producto;
import com.tienda.model.Vendedor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class PerfilPublicoVendedorPanel extends ScrollPane {

    private final TiendaController controller;
    private final VBox containerCentral;
    private final GridPane gridProductos;
    private Vendedor vendedorSeleccionado;

    public PerfilPublicoVendedorPanel(TiendaController controller) {
        this.controller = controller;

        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        containerCentral = new VBox(24);
        containerCentral.setPadding(new Insets(20));

        gridProductos = new GridPane();
        gridProductos.setHgap(20);
        gridProductos.setVgap(20);
        gridProductos.setPadding(new Insets(10, 5, 20, 5));

        this.setContent(containerCentral);
    }

    public void cargarVendedor(String idVendedor) {
        containerCentral.getChildren().clear();
        gridProductos.getChildren().clear();

        vendedorSeleccionado = controller.obtenerVendedorPorId(idVendedor);

        if (vendedorSeleccionado == null) {
            Label lblError = new Label("No se pudo encontrar al vendedor.");
            containerCentral.getChildren().add(lblError);
            return;
        }

        // --- ENCABEZADO DEL VENDEDOR ---
        VBox cardTaller = new VBox(16);
        cardTaller.getStyleClass().add("section-card");

        HBox headerTaller = new HBox(16);
        headerTaller.setAlignment(Pos.CENTER_LEFT);

        Label iconTaller = new Label("🎨");
        iconTaller.setStyle("-fx-font-size: 36px; -fx-background-color: #F7EBE6; -fx-padding: 10 16 10 16; -fx-background-radius: 12px;");

        VBox titleBox = new VBox(4);
        Label lblTienda = new Label(vendedorSeleccionado.getNombreTienda());
        lblTienda.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        Label lblArtesano = new Label("Artesano: " + vendedorSeleccionado.getArtesanoNombre() + " | Calificación: ⭐ " + vendedorSeleccionado.getCalificacion());
        lblArtesano.setStyle("-fx-font-size: 13px; -fx-text-fill: #6E6862;");

        titleBox.getChildren().addAll(lblTienda, lblArtesano);
        headerTaller.getChildren().addAll(iconTaller, titleBox);

        Label lblHistoria = new Label("Acerca del taller: " + vendedorSeleccionado.getHistoriaTaller());
        lblHistoria.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C1E16; -fx-font-style: italic;");
        lblHistoria.setWrapText(true);

        cardTaller.getChildren().addAll(headerTaller, new Separator(), lblHistoria);

        // --- PRODUCTOS ---
        VBox cardProductos = new VBox(16);
        cardProductos.getStyleClass().add("section-card");

        Label lblProductos = new Label("Obras Disponibles");
        lblProductos.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        renderizarProductos();

        cardProductos.getChildren().addAll(lblProductos, new Separator(), gridProductos);

        containerCentral.getChildren().addAll(cardTaller, cardProductos);
    }

    private void renderizarProductos() {
        List<Producto> productos = vendedorSeleccionado.getProductosEnVenta();

        if (productos.isEmpty()) {
            Label empty = new Label("Este vendedor no tiene productos disponibles actualmente.");
            empty.setStyle("-fx-text-fill: #6E6862; -fx-font-style: italic;");
            gridProductos.add(empty, 0, 0);
            return;
        }

        int columnasMax = 3;
        int col = 0;
        int row = 0;

        for (Producto prod : productos) {
            VBox card = crearTarjetaProducto(prod);
            gridProductos.add(card, col, row);

            col++;
            if (col >= columnasMax) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearTarjetaProducto(Producto prod) {
        VBox card = new VBox(12);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(260);

        ImageView imageView;
        try {
            String img = prod.getImagen();
            String imagePath = "/images/" + img + (img.contains(".") ? "" : ".png");
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView = new ImageView(image);
        } catch (Exception e) {
            imageView = new ImageView();
        }
        imageView.setFitWidth(260);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);
        
        Rectangle clip = new Rectangle(260, 140);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        HBox topBadges = new HBox(8);
        topBadges.setAlignment(Pos.CENTER_LEFT);
        Label badgeCat = new Label(prod.getCategoria());
        badgeCat.getStyleClass().add("badge-category");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label badgeStock = new Label(prod.tieneStockDisponible() ? "Stock: " + prod.getStock() : "Agotado");
        badgeStock.getStyleClass().add(prod.tieneStockDisponible() ? "badge-stock-in" : "badge-stock-out");
        topBadges.getChildren().addAll(badgeCat, spacer, badgeStock);

        Label lblTitulo = new Label(prod.getNombre());
        lblTitulo.getStyleClass().add("product-title");
        lblTitulo.setWrapText(true);

        Label lblDesc = new Label(prod.getDescripcion());
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(36);

        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setPadding(new Insets(8, 0, 0, 0));

        Label lblPrecio = new Label(prod.obtenerPrecioFormateado());
        lblPrecio.getStyleClass().add("product-price");

        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);

        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().add("btn-primary");
        btnAgregar.setOnAction(e -> controller.agregarAlCarrito(prod));

        bottomRow.getChildren().addAll(lblPrecio, spacerBottom, btnAgregar);

        card.getChildren().addAll(imageView, topBadges, lblTitulo, lblDesc, bottomRow);
        return card;
    }
}
