package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.Producto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * Vista de Catálogo de Productos (Patrón MVC - View).
 * Muestra el listado visual en cuadrícula (GridPane/Cards) de artesanías disponibles,
 * con buscador integrado y filtros interactivos por categoría.
 */
public class CatalogoPanel extends BorderPane {

    private final TiendaController controller;
    private final GridPane gridProductos;
    private final TextField txtBusqueda;
    private String categoriaSeleccionada = "Todos";
    private final HBox containerFiltros;

    public CatalogoPanel(TiendaController controller) {
        this.controller = controller;

        this.setPadding(new Insets(20));

        // --- BARRA SUPERIOR DE BÚSQUEDA Y FILTROS ---
        VBox topBox = new VBox(15);
        topBox.setPadding(new Insets(0, 0, 20, 0));

        // Buscador
        HBox searchBar = new HBox(12);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        Label lblBuscarIcon = new Label("🔍");
        lblBuscarIcon.setStyle("-fx-font-size: 16px;");

        txtBusqueda = new TextField();
        txtBusqueda.setPromptText("Buscar artesanías por nombre, descripción o artesano...");
        txtBusqueda.setPrefWidth(450);
        HBox.setHgrow(txtBusqueda, Priority.ALWAYS);

        // Evento de búsqueda dinámica al escribir
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> renderizarCatalogo());

        searchBar.getChildren().addAll(lblBuscarIcon, txtBusqueda);

        // Chips de Filtros de Categorías
        containerFiltros = new HBox(10);
        containerFiltros.setAlignment(Pos.CENTER_LEFT);

        String[] categorias = {"Todos", "Cerámica", "Textil", "Madera", "Joyería", "Cuero"};
        for (String cat : categorias) {
            Button btnCat = new Button(cat);
            btnCat.getStyleClass().add("btn-filter");
            if (cat.equals("Todos")) {
                btnCat.getStyleClass().add("btn-filter-active");
            }

            btnCat.setOnAction(e -> {
                categoriaSeleccionada = cat;
                actualizarEstiloFiltros(btnCat);
                renderizarCatalogo();
            });

            containerFiltros.getChildren().add(btnCat);
        }

        topBox.getChildren().addAll(searchBar, containerFiltros);
        this.setTop(topBox);

        // --- ÁREA CENTRAL: GRID RESPONSIVO DE TARJETAS DE PRODUCTO ---
        gridProductos = new GridPane();
        gridProductos.setHgap(20);
        gridProductos.setVgap(20);
        gridProductos.setPadding(new Insets(10, 5, 20, 5));

        ScrollPane scrollPane = new ScrollPane(gridProductos);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.setCenter(scrollPane);

        // Renderizado inicial
        renderizarCatalogo();

        // Escuchar cambios en la lista observable de productos para actualizar en tiempo real
        controller.getCatalogoProductos().addListener((javafx.collections.ListChangeListener.Change<? extends Producto> c) -> renderizarCatalogo());
    }

    private void actualizarEstiloFiltros(Button botonActivo) {
        containerFiltros.getChildren().forEach(node -> {
            if (node instanceof Button b) {
                b.getStyleClass().remove("btn-filter-active");
            }
        });
        botonActivo.getStyleClass().add("btn-filter-active");
    }

    /**
     * Reconstruye las tarjetas de productos según los filtros activos.
     */
    public void renderizarCatalogo() {
        gridProductos.getChildren().clear();

        List<Producto> listaFiltrada = controller.filtrarProductos(categoriaSeleccionada, txtBusqueda.getText());

        if (listaFiltrada.isEmpty()) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(60));

            Label iconEmpty = new Label("🏜️");
            iconEmpty.setStyle("-fx-font-size: 48px;");

            Label lblMensaje = new Label("No se encontraron piezas artesanales con ese criterio.");
            lblMensaje.setStyle("-fx-font-size: 16px; -fx-text-fill: #6E6862; -fx-font-weight: bold;");

            emptyBox.getChildren().addAll(iconEmpty, lblMensaje);
            gridProductos.add(emptyBox, 0, 0);
            return;
        }

        int columnasMax = 3;
        int col = 0;
        int row = 0;

        for (Producto prod : listaFiltrada) {
            VBox card = crearTarjetaProducto(prod);
            gridProductos.add(card, col, row);

            col++;
            if (col >= columnasMax) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Genera una tarjeta de producto moderna con dibujo vectorial canvas personalizado.
     */
    private VBox crearTarjetaProducto(Producto prod) {
        VBox card = new VBox(12);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(260);

        // Imagen generada del producto
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
        
        // Clip redondeado
        Rectangle clip = new Rectangle(260, 140);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        // Header de la tarjeta con Badges de Categoría y Stock
        HBox topBadges = new HBox(8);
        topBadges.setAlignment(Pos.CENTER_LEFT);

        Label badgeCat = new Label(prod.getCategoria());
        badgeCat.getStyleClass().add("badge-category");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badgeStock = new Label(prod.tieneStockDisponible() ? "Stock: " + prod.getStock() : "Agotado");
        badgeStock.getStyleClass().add(prod.tieneStockDisponible() ? "badge-stock-in" : "badge-stock-out");

        topBadges.getChildren().addAll(badgeCat, spacer, badgeStock);

        // Información principal
        Label lblTitulo = new Label(prod.getNombre());
        lblTitulo.getStyleClass().add("product-title");
        lblTitulo.setWrapText(true);

        Hyperlink linkArtesano = new Hyperlink("Por " + prod.getArtesanoNombre());
        linkArtesano.getStyleClass().add("product-artisan");
        linkArtesano.setStyle("-fx-border-color: transparent; -fx-padding: 0; -fx-underline: true; -fx-text-fill: #6E6862;");
        linkArtesano.setOnAction(e -> controller.solicitarCambioVista("VER_PERFIL_PUBLICO:" + prod.getVendedorId()));

        Label lblDesc = new Label(prod.getDescripcion());
        lblDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(36);

        // Fila Inferior con Precio y Botón de Acción
        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setPadding(new Insets(8, 0, 0, 0));

        Label lblPrecio = new Label(prod.obtenerPrecioFormateado());
        lblPrecio.getStyleClass().add("product-price");

        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);

        Button btnAgregar = new Button("🛒 Agregar");
        btnAgregar.getStyleClass().add("btn-primary");

        btnAgregar.setOnAction(e -> controller.agregarAlCarrito(prod));

        bottomRow.getChildren().addAll(lblPrecio, spacerBottom, btnAgregar);

        card.getChildren().addAll(imageView, topBadges, lblTitulo, linkArtesano, lblDesc, bottomRow);
        return card;
    }


}
