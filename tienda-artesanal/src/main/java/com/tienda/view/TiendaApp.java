package com.tienda.view;

import com.tienda.controller.TiendaController;
import com.tienda.model.ItemCarrito;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Punto de entrada principal (JavaFX Application Class) para la "Tienda Artesanal".
 * Configura la ventana principal (Stage), el layout con Sidebar de navegación,
 * Header superior, área de contenido dinámico y el sistema overlay de notificaciones Toast.
 */
public class TiendaApp extends Application {

    private TiendaController controller;
    private BorderPane mainLayout;
    private StackPane contentStack;
    private VBox toastContainer;

    private CatalogoPanel panelCatalogo;
    private PerfilCompradorPanel panelComprador;
    private PerfilVendedorPanel panelVendedor;
    private CarritoCheckoutPanel panelCarrito;
    private PerfilPublicoVendedorPanel panelPerfilVendedorPublico;
    private DirectorioVendedoresPanel panelDirectorio;

    private Button btnNavCatalogo;
    private Button btnNavDirectorio;
    private Button btnNavCarrito;
    private Button btnNavComprador;
    private Button btnNavVendedor;
    private Label badgeCarritoCount;

    private Label lblHeaderTitle;
    private Label lblHeaderSubtitle;

    @Override
    public void start(Stage primaryStage) {
        // 1. Inicializar el Controlador MVC
        controller = new TiendaController();

        // 2. Crear Layout Principal (StackPane base para soportar la capa Toast)
        StackPane rootPane = new StackPane();

        mainLayout = new BorderPane();
        rootPane.getChildren().add(mainLayout);

        // 3. Configurar Notificaciones Toast en Capa Superior
        toastContainer = new VBox(10);
        toastContainer.setAlignment(Pos.BOTTOM_RIGHT);
        toastContainer.setPadding(new Insets(20));
        toastContainer.setMouseTransparent(true);
        rootPane.getChildren().add(toastContainer);

        controller.setNotificadorMensajes(this::mostrarToastNotification);
        controller.setCambiadorVista(this::manejarSolicitudVista);

        // 4. Instanciar Vistas
        panelCatalogo = new CatalogoPanel(controller);
        panelComprador = new PerfilCompradorPanel(controller);
        panelVendedor = new PerfilVendedorPanel(controller);
        panelCarrito = new CarritoCheckoutPanel(controller);
        panelPerfilVendedorPublico = new PerfilPublicoVendedorPanel(controller);
        panelDirectorio = new DirectorioVendedoresPanel(controller);

        contentStack = new StackPane();
        contentStack.getChildren().addAll(panelCatalogo, panelDirectorio, panelComprador, panelVendedor, panelCarrito, panelPerfilVendedorPublico);

        mainLayout.setCenter(contentStack);

        // 5. Configurar Sidebar y Header
        mainLayout.setLeft(crearSidebarNavegacion());
        mainLayout.setTop(crearHeaderSuperior());

        // 6. Activar Vista Inicial (Catálogo)
        cambiarVista("CATALOGO");

        // 7. Configurar Escena JavaFX y Cargar CSS
        Scene scene = new Scene(rootPane, 1240, 800);
        
        try {
            String cssPath = getClass().getResource("/com/tienda/view/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception ex) {
            System.err.println("Advertencia: No se pudo cargar el archivo CSS externo: " + ex.getMessage());
        }

        primaryStage.setTitle("Tienda Artesanal — Mercado de Piezas Únicas & Arte Tradicional");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(1000);
        primaryStage.show();
    }

    /**
     * Construye el Menú Lateral (Sidebar) con botones de navegación e indicadores.
     */
    private VBox crearSidebarNavegacion() {
        VBox sidebar = new VBox(16);
        sidebar.getStyleClass().add("sidebar");

        // Branding / Logotipo
        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(0, 0, 16, 0));

        Label lblBrand = new Label("Tienda Artesanal");
        lblBrand.getStyleClass().add("brand-title");

        Label lblSub = new Label("Mercado de Arte Tradicional");
        lblSub.getStyleClass().add("brand-subtitle");

        brandBox.getChildren().addAll(lblBrand, lblSub);

        // Botones de Navegación
        btnNavCatalogo = crearBotonNav("Catálogo de Artesanías");
        btnNavDirectorio = crearBotonNav("Directorio de Artesanos");
        
        // Botón Carrito con Badge Dinámico
        btnNavCarrito = crearBotonNav("Carrito de Compras");
        HBox boxCarritoNav = new HBox(8);
        boxCarritoNav.setAlignment(Pos.CENTER_LEFT);
        badgeCarritoCount = new Label("0");
        badgeCarritoCount.getStyleClass().add("nav-badge");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        btnNavCarrito.setMaxWidth(Double.MAX_VALUE);

        boxCarritoNav.getChildren().addAll(btnNavCarrito, sp, badgeCarritoCount);
        boxCarritoNav.setOnMouseClicked(e -> cambiarVista("CARRITO"));

        btnNavComprador = crearBotonNav("Perfil Comprador");
        btnNavVendedor = crearBotonNav("Panel del Artesano");

        btnNavCatalogo.setOnAction(e -> cambiarVista("CATALOGO"));
        btnNavDirectorio.setOnAction(e -> cambiarVista("DIRECTORIO"));
        btnNavCarrito.setOnAction(e -> cambiarVista("CARRITO"));
        btnNavComprador.setOnAction(e -> cambiarVista("COMPRADOR"));
        btnNavVendedor.setOnAction(e -> cambiarVista("VENDEDOR"));

        VBox navMenu = new VBox(8);
        navMenu.getChildren().addAll(btnNavCatalogo, btnNavDirectorio, boxCarritoNav, btnNavComprador, btnNavVendedor);

        // Actualización dinámica del contador del carrito
        controller.getCarritoCompras().addListener((ListChangeListener.Change<? extends ItemCarrito> c) -> {
            int totalArticulos = controller.obtenerCantidadTotalArticulosCarrito();
            badgeCarritoCount.setText(String.valueOf(totalArticulos));
            badgeCarritoCount.setVisible(totalArticulos > 0);
        });

        Region spacerBottom = new Region();
        VBox.setVgrow(spacerBottom, Priority.ALWAYS);

        // Pie de Sidebar con información de versión
        VBox footerBox = new VBox(4);
        Label lblVer = new Label("v1.0.0 • JavaFX MVC");
        lblVer.setStyle("-fx-text-fill: #7E7267; -fx-font-size: 11px;");
        footerBox.getChildren().add(lblVer);

        sidebar.getChildren().addAll(brandBox, navMenu, spacerBottom, footerBox);
        return sidebar;
    }

    private Button crearBotonNav(String texto) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    /**
     * Construye el Header Superior con título de la vista y perfil activo.
     */
    private HBox crearHeaderSuperior() {
        HBox header = new HBox(16);
        header.getStyleClass().add("header-bar");
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        lblHeaderTitle = new Label("Catálogo de Piezas Únicas");
        lblHeaderTitle.getStyleClass().add("header-title");

        lblHeaderSubtitle = new Label("Explora creaciones hechas a mano por maestros artesanos de todo el país.");
        lblHeaderSubtitle.getStyleClass().add("header-subtitle");

        titleBox.getChildren().addAll(lblHeaderTitle, lblHeaderSubtitle);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        // Perfil Usuario Activo en Header
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER);
        userBox.setStyle("-fx-background-color: #F8F5F0; -fx-padding: 6 14 6 14; -fx-background-radius: 20px; -fx-border-color: #E5DEC9; -fx-border-radius: 20px;");

        Circle circleAvatar = new Circle(14, javafx.scene.paint.Color.web("#C85A32"));
        Label lblUser = new Label(controller.getCompradorActual().getNombre());
        lblUser.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #2C1E16;");

        userBox.getChildren().addAll(circleAvatar, lblUser);

        header.getChildren().addAll(titleBox, sp, userBox);
        return header;
    }

    /**
     * Alterna la visibilidad de los paneles en el área central.
     */
    private void cambiarVista(String claveVista) {
        // Desactivar visibilidad de todos
        panelCatalogo.setVisible(false);
        panelComprador.setVisible(false);
        panelVendedor.setVisible(false);
        panelCarrito.setVisible(false);
        panelPerfilVendedorPublico.setVisible(false);
        panelDirectorio.setVisible(false);

        // Reset de estilos de navegación
        btnNavCatalogo.getStyleClass().remove("nav-button-active");
        btnNavDirectorio.getStyleClass().remove("nav-button-active");
        btnNavCarrito.getStyleClass().remove("nav-button-active");
        btnNavComprador.getStyleClass().remove("nav-button-active");
        btnNavVendedor.getStyleClass().remove("nav-button-active");

        switch (claveVista) {
            case "CATALOGO" -> {
                panelCatalogo.setVisible(true);
                panelCatalogo.renderizarCatalogo();
                btnNavCatalogo.getStyleClass().add("nav-button-active");
                lblHeaderTitle.setText("Catálogo de Piezas Únicas");
                lblHeaderSubtitle.setText("Explora creaciones hechas a mano por maestros artesanos de todo el país.");
            }
            case "CARRITO" -> {
                panelCarrito.setVisible(true);
                panelCarrito.renderizarCarrito();
                btnNavCarrito.getStyleClass().add("nav-button-active");
                lblHeaderTitle.setText("Carrito de Compras y Checkout");
                lblHeaderSubtitle.setText("Confirma tus productos, calcula envío y procesa tu pago seguro.");
            }
            case "COMPRADOR" -> {
                panelComprador.setVisible(true);
                panelComprador.renderizarHistorial();
                btnNavComprador.getStyleClass().add("nav-button-active");
                lblHeaderTitle.setText("Perfil del Comprador");
                lblHeaderSubtitle.setText("Consulta tu información de entrega e historial de compras activas.");
            }
            case "VENDEDOR" -> {
                panelVendedor.setVisible(true);
                btnNavVendedor.getStyleClass().add("nav-button-active");
                lblHeaderTitle.setText("Panel de Administración de Taller");
                lblHeaderSubtitle.setText("Gestiona tus obras y publicaciones.");
            }
            case "DIRECTORIO" -> {
                panelDirectorio.setVisible(true);
                btnNavDirectorio.getStyleClass().add("nav-button-active");
                lblHeaderTitle.setText("Comunidad de Artesanos");
                lblHeaderSubtitle.setText("Descubre las mentes creativas detrás del mercado.");
            }
        }
    }

    private void manejarSolicitudVista(String solicitud) {
        if ("Ir_Catalogo".equalsIgnoreCase(solicitud)) {
            cambiarVista("CATALOGO");
        } else if ("Navegar_Historial".equalsIgnoreCase(solicitud)) {
            cambiarVista("COMPRADOR");
        } else if ("NOTIFICAR_DATOS_ACTUALIZADOS".equals(solicitud)) {
            mostrarToastNotification("Datos de perfil y dirección actualizados correctamente.");
        } else if ("ERROR_NUMERO_INVALIDO".equals(solicitud)) {
            mostrarToastNotification("Por favor ingresa un precio numérico válido (ej. 850.50).");
        } else if (solicitud != null && solicitud.startsWith("VER_PERFIL_PUBLICO:")) {
            String idVendedor = solicitud.substring("VER_PERFIL_PUBLICO:".length());
            panelPerfilVendedorPublico.cargarVendedor(idVendedor);
            
            panelCatalogo.setVisible(false);
            panelComprador.setVisible(false);
            panelVendedor.setVisible(false);
            panelCarrito.setVisible(false);
            panelDirectorio.setVisible(false);
            
            panelPerfilVendedorPublico.setVisible(true);
            
            btnNavCatalogo.getStyleClass().remove("nav-button-active");
            btnNavDirectorio.getStyleClass().remove("nav-button-active");
            btnNavCarrito.getStyleClass().remove("nav-button-active");
            btnNavComprador.getStyleClass().remove("nav-button-active");
            btnNavVendedor.getStyleClass().remove("nav-button-active");
            
            lblHeaderTitle.setText("Perfil de Taller Artesanal");
            lblHeaderSubtitle.setText("Conoce más sobre este taller y explora todas sus creaciones.");
        }
    }

    /**
     * Despliega una alerta Toast animada en la esquina inferior de la pantalla.
     */
    private void mostrarToastNotification(String mensaje) {
        Label toast = new Label(mensaje);
        toast.getStyleClass().add("toast-notification");
        toast.setOpacity(0.0);

        toastContainer.getChildren().add(toast);

        // Animación FadeIn -> Pause -> FadeOut -> Remove
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(3.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), toast);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> toastContainer.getChildren().remove(toast));

        fadeIn.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> fadeOut.play());

        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
