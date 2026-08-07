package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.Producto;
import com.tienda.model.Vendedor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Panel de Administración para el Vendedor / Artesano (Patrón MVC - View).
 * Permite gestionar el taller artesanal, revisar productos publicados y dar de alta nuevas obras.
 */
public class PerfilVendedorPanel extends ScrollPane {

    private final TiendaController controller;
    private final TextField txtNombreProducto;
    private final TextArea txtDescripcionProducto;
    private final TextField txtPrecio;
    private final Spinner<Integer> spinStock;
    private final ComboBox<String> comboCategoria;
    private final VBox containerProductosVendedor;
    
    private File imagenSeleccionada;
    private Label lblImagenSeleccionada;

    public PerfilVendedorPanel(TiendaController controller) {
        this.controller = controller;

        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(20));

        Vendedor vendedor = controller.getVendedorActual();

        // --- TARJETA 1: ENCABEZADO DEL TALLER Y STATS ---
        VBox cardTaller = new VBox(16);
        cardTaller.getStyleClass().add("section-card");

        HBox headerTaller = new HBox(16);
        headerTaller.setAlignment(Pos.CENTER_LEFT);

        Label iconTaller = new Label("🎨");
        iconTaller.setStyle("-fx-font-size: 36px; -fx-background-color: #F7EBE6; -fx-padding: 10 16 10 16; -fx-background-radius: 12px;");

        VBox titleBox = new VBox(4);
        Label lblTienda = new Label(vendedor.getNombreTienda());
        lblTienda.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        Label lblArtesano = new Label("Maestro Artesano: " + vendedor.getArtesanoNombre() + " | Contacto: " + vendedor.getContacto());
        lblArtesano.setStyle("-fx-font-size: 13px; -fx-text-fill: #6E6862;");

        titleBox.getChildren().addAll(lblTienda, lblArtesano);
        headerTaller.getChildren().addAll(iconTaller, titleBox);

        // Stats Cards
        HBox statsBox = new HBox(16);
        statsBox.getChildren().addAll(
            crearStatCard("⭐ Calificación Taller", String.format("%.1f / 5.0", vendedor.getCalificacion()), "#D99B26"),
            crearStatCard("📦 Productos Activos", String.valueOf(vendedor.getProductosEnVenta().size()), "#C85A32"),
            crearStatCard("🏷️ Registro Artesano", vendedor.getId(), "#5E6F52")
        );

        Label lblHistoria = new Label("Reseña del Taller: " + vendedor.getHistoriaTaller());
        lblHistoria.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C1E16; -fx-font-style: italic;");
        lblHistoria.setWrapText(true);

        cardTaller.getChildren().addAll(headerTaller, statsBox, new Separator(), lblHistoria);

        // --- TARJETA 2: FORMULARIO PARA PUBLICAR NUEVA ARTESANÍA ---
        VBox cardPublicar = new VBox(16);
        cardPublicar.getStyleClass().add("section-card");

        Label lblTituloForm = new Label("✨ Publicar Nueva Pieza Artesanal");
        lblTituloForm.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        GridPane gridForm = new GridPane();
        gridForm.setHgap(16);
        gridForm.setVgap(14);

        txtNombreProducto = new TextField();
        txtNombreProducto.setPromptText("Ej. Alebrije de Zorro Tallado");

        txtDescripcionProducto = new TextArea();
        txtDescripcionProducto.setPromptText("Describe los materiales orgánicos, técnica de elaborado y significado de la obra...");
        txtDescripcionProducto.setPrefRowCount(3);
        txtDescripcionProducto.setWrapText(true);

        comboCategoria = new ComboBox<>();
        comboCategoria.getItems().addAll("Cerámica", "Textil", "Madera", "Joyería", "Cuero");
        comboCategoria.getSelectionModel().selectFirst();

        txtPrecio = new TextField();
        txtPrecio.setPromptText("Ej. 1250.00");

        spinStock = new Spinner<>(1, 100, 5);
        spinStock.setEditable(true);

        gridForm.add(new Label("Nombre de la Pieza:"), 0, 0);
        gridForm.add(txtNombreProducto, 1, 0);

        gridForm.add(new Label("Categoría Disciplina:"), 0, 1);
        gridForm.add(comboCategoria, 1, 1);

        gridForm.add(new Label("Descripción Detallada:"), 0, 2);
        gridForm.add(txtDescripcionProducto, 1, 2);

        gridForm.add(new Label("Precio ($ MXN):"), 0, 3);
        gridForm.add(txtPrecio, 1, 3);

        gridForm.add(new Label("Unidades en Stock:"), 0, 4);
        gridForm.add(spinStock, 1, 4);

        Label lblImg = new Label("Imagen del Producto:");
        Button btnImagen = new Button("📁 Seleccionar Imagen...");
        btnImagen.getStyleClass().add("btn-secondary");
        lblImagenSeleccionada = new Label("Ninguna imagen seleccionada");
        lblImagenSeleccionada.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862; -fx-font-style: italic;");

        btnImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen del Producto");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showOpenDialog(btnImagen.getScene().getWindow());
            if (file != null) {
                imagenSeleccionada = file;
                lblImagenSeleccionada.setText(file.getName());
            }
        });

        HBox imageBox = new HBox(10);
        imageBox.setAlignment(Pos.CENTER_LEFT);
        imageBox.getChildren().addAll(btnImagen, lblImagenSeleccionada);
        
        gridForm.add(lblImg, 0, 5);
        gridForm.add(imageBox, 1, 5);

        Button btnPublicar = new Button("🚀 Publicar en el Mercado");
        btnPublicar.getStyleClass().add("btn-success");
        btnPublicar.setOnAction(e -> procesarPublicacion());

        cardPublicar.getChildren().addAll(lblTituloForm, new Separator(), gridForm, btnPublicar);

        // --- TARJETA 3: LISTADO DE PRODUCTOS EN VENTA ---
        VBox cardProductos = new VBox(16);
        cardProductos.getStyleClass().add("section-card");

        Label lblTituloProductos = new Label("🏺 Productos Publicados por Tu Taller");
        lblTituloProductos.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");

        containerProductosVendedor = new VBox(12);
        renderizarProductosVendedor();

        cardProductos.getChildren().addAll(lblTituloProductos, new Separator(), containerProductosVendedor);

        mainContent.getChildren().addAll(cardTaller, cardPublicar, cardProductos);
        this.setContent(mainContent);
    }

    private VBox crearStatCard(String titulo, String valor, String colorHex) {
        VBox box = new VBox(4);
        box.setStyle(String.format(
            "-fx-background-color: #F8F5F0; -fx-padding: 12 18 12 18; -fx-background-radius: 10px; -fx-border-color: %s; -fx-border-width: 0 0 0 4px;",
            colorHex
        ));
        Label lblT = new Label(titulo);
        lblT.setStyle("-fx-font-size: 11px; -fx-text-fill: #6E6862; -fx-font-weight: bold;");
        Label lblV = new Label(valor);
        lblV.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");
        box.getChildren().addAll(lblT, lblV);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void procesarPublicacion() {
        try {
            String nombre = txtNombreProducto.getText();
            String desc = txtDescripcionProducto.getText();
            String cat = comboCategoria.getValue();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = spinStock.getValue();

            String imageName = "";
            if (imagenSeleccionada != null) {
                try {
                    String targetDir = "src/main/resources/images/";
                    File dir = new File(targetDir);
                    if (!dir.exists()) dir.mkdirs();
                    
                    String originalName = imagenSeleccionada.getName();
                    String extension = "";
                    int i = originalName.lastIndexOf('.');
                    if (i > 0) {
                        extension = originalName.substring(i);
                    }
                    imageName = "img_" + System.currentTimeMillis() + extension;
                    
                    Path dest = Paths.get(targetDir + imageName);
                    Files.copy(imagenSeleccionada.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    
                    try {
                        String runtimeDir = getClass().getResource("/images/").toURI().getPath();
                        if (runtimeDir != null) {
                            Path runtimeDest = Paths.get(runtimeDir, imageName);
                            Files.copy(imagenSeleccionada.toPath(), runtimeDest, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (Exception ignored) {
                        // Ignorar
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            boolean exito = controller.publicarProducto(nombre, desc, precio, stock, cat, imageName);
            if (exito) {
                // Limpiar campos
                txtNombreProducto.clear();
                txtDescripcionProducto.clear();
                txtPrecio.clear();
                spinStock.getValueFactory().setValue(5);
                imagenSeleccionada = null;
                lblImagenSeleccionada.setText("Ninguna imagen seleccionada");
                renderizarProductosVendedor();
            }
        } catch (NumberFormatException ex) {
            controller.solicitarCambioVista("ERROR_NUMERO_INVALIDO");
        }
    }

    public void renderizarProductosVendedor() {
        containerProductosVendedor.getChildren().clear();
        List<Producto> productos = controller.getVendedorActual().getProductosEnVenta();

        if (productos.isEmpty()) {
            Label empty = new Label("No has publicado productos aún.");
            empty.setStyle("-fx-text-fill: #6E6862; -fx-font-style: italic;");
            containerProductosVendedor.getChildren().add(empty);
            return;
        }

        for (Producto p : productos) {
            HBox itemRow = new HBox(12);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setStyle("-fx-background-color: #F8F5F0; -fx-padding: 12px; -fx-background-radius: 8px;");

            Label lblNombre = new Label(p.getNombre());
            lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2C1E16;");

            Label badgeCat = new Label(p.getCategoria());
            badgeCat.getStyleClass().add("badge-category");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Label lblStock = new Label("Stock: " + p.getStock() + " uds.");
            lblStock.setStyle("-fx-font-size: 12px; -fx-text-fill: #6E6862;");

            Label lblPrecio = new Label(p.obtenerPrecioFormateado());
            lblPrecio.setStyle("-fx-font-weight: bold; -fx-text-fill: #C85A32; -fx-font-size: 14px;");

            Button btnEliminar = new Button("Eliminar");
            btnEliminar.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; -fx-border-color: #ef9a9a; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-cursor: hand;");
            btnEliminar.setOnAction(e -> {
                controller.eliminarProductoVendedor(p);
                renderizarProductosVendedor();
            });

            itemRow.getChildren().addAll(lblNombre, badgeCat, sp, lblStock, lblPrecio, btnEliminar);
            containerProductosVendedor.getChildren().add(itemRow);
        }
    }
}
