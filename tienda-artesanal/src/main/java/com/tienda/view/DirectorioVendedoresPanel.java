package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.Vendedor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class DirectorioVendedoresPanel extends ScrollPane {

    private final TiendaController controller;
    private final GridPane gridVendedores;

    public DirectorioVendedoresPanel(TiendaController controller) {
        this.controller = controller;

        this.setFitToWidth(true);
        this.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox containerCentral = new VBox(24);
        containerCentral.setPadding(new Insets(20));

        Label lblTitulo = new Label("👥 Directorio de Artesanos");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");
        
        Label lblSubtitulo = new Label("Conoce a los maestros artesanos detrás de cada creación.");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #6E6862;");

        gridVendedores = new GridPane();
        gridVendedores.setHgap(20);
        gridVendedores.setVgap(20);
        gridVendedores.setPadding(new Insets(10, 5, 20, 5));

        renderizarDirectorio();

        containerCentral.getChildren().addAll(lblTitulo, lblSubtitulo, new Separator(), gridVendedores);
        this.setContent(containerCentral);
    }

    private void renderizarDirectorio() {
        List<Vendedor> vendedores = controller.getDirectorioVendedores();
        int columnasMax = 2;
        int col = 0;
        int row = 0;

        for (Vendedor v : vendedores) {
            VBox card = crearTarjetaVendedor(v);
            gridVendedores.add(card, col, row);

            col++;
            if (col >= columnasMax) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearTarjetaVendedor(Vendedor v) {
        VBox card = new VBox(12);
        card.getStyleClass().add("section-card");
        card.setPrefWidth(400);

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("🎨");
        icon.setStyle("-fx-font-size: 24px; -fx-background-color: #F7EBE6; -fx-padding: 8; -fx-background-radius: 8px;");

        VBox titleBox = new VBox(4);
        Label lblTienda = new Label(v.getNombreTienda());
        lblTienda.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1E16;");
        
        Label lblArtesano = new Label(v.getArtesanoNombre() + " | ⭐ " + v.getCalificacion());
        lblArtesano.setStyle("-fx-font-size: 13px; -fx-text-fill: #6E6862;");
        titleBox.getChildren().addAll(lblTienda, lblArtesano);

        header.getChildren().addAll(icon, titleBox);

        Label lblDesc = new Label(v.getHistoriaTaller());
        lblDesc.setStyle("-fx-font-size: 13px; -fx-text-fill: #6E6862;");
        lblDesc.setWrapText(true);
        lblDesc.setPrefHeight(60);

        Button btnVerPerfil = new Button("Ver Taller y Productos");
        btnVerPerfil.getStyleClass().add("btn-secondary");
        btnVerPerfil.setMaxWidth(Double.MAX_VALUE);
        btnVerPerfil.setOnAction(e -> controller.solicitarCambioVista("VER_PERFIL_PUBLICO:" + v.getId()));

        card.getChildren().addAll(header, new Separator(), lblDesc, btnVerPerfil);
        return card;
    }
}
